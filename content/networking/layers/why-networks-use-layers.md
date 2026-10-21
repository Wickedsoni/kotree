---
id: networking/layers/why-networks-use-layers
title: Why does a message pass through several layers before it leaves my phone?
status: live
difficulty: medium
order: 3
requires: [networking/how-the-internet-works/how-data-travels-in-packets]
summary: Each layer wraps the message with its own label on the way out. Each layer reads only its own label on the way in.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [envelope-layers]
playground: false
fruit: false
upnext: false
---

## The question

Your app asks a server for a picture. Between your code and the Wi-Fi radio there are several steps, and each one does a different job. Why not send the request straight out? And when a request fails, why does the error name usually point at only one of those steps?

## Mental model

Think of a letter that travels inside an envelope, inside a courier box, inside a lorry.

- The letter is the message your app wants to send.
- The envelope has a label that says which program on the other computer should get the letter.
- The courier box has a label with the address of the other computer.
- The lorry has a label with the next stop on the road.
- A handler stands at each step. Each step is called a **layer**. A handler reads only the label on the outside and does what it says.

On the way out, the wrappers close one by one: envelope, courier box, lorry. On the way in, they open one by one in the opposite order. A handler never opens a wrapper that belongs to someone else.

**Where the analogy stops:** A real layer writes a small label in front of the data. It does not put the data in a box. The handlers on your phone are pieces of software, not people. A message may also be cut into pieces on the way, which the packets lesson shows. And some devices in the middle, such as a proxy (a go-between computer), may look deeper than a lorry driver would. MDN says proxies work at the application layers.

## See it move

```sim
id: envelope-layers
template: flow
title: A letter closing and opening its wrappers
intro: "A letter waits at your app. Four handlers stand on the sending side and four on the receiving side. No wrapper is on the letter yet."
stations:
  - { id: app-out, label: "Your app: writes the letter" }
  - { id: transport-out, label: "Transport: adds the envelope" }
  - { id: internet-out, label: "Internet: adds the courier box" }
  - { id: link-out, label: "Link: adds the lorry" }
  - { id: link-in, label: "Link: reads the lorry" }
  - { id: internet-in, label: "Internet: reads the courier box" }
  - { id: transport-in, label: "Transport: reads the envelope" }
  - { id: app-in, label: "Server app: reads the letter" }
paths:
  - [app-out, transport-out, internet-out, link-out, link-in, internet-in, transport-in, app-in]
items:
  - { id: letter, label: "Letter", at: app-out }
actions:
  - id: next
    label: Next stop
    cases:
      - when: { at: { letter: app-out } }
        do:
          - move: { letter: transport-out }
          - label: { letter: "Letter in envelope" }
          - light: [transport-out]
          - say: "The transport handler seals the letter in an envelope and writes on it which program should get the letter."
      - when: { at: { letter: transport-out } }
        do:
          - move: { letter: internet-out }
          - label: { letter: "Letter in envelope in courier box" }
          - light: [internet-out]
          - say: "The internet handler puts the envelope in a courier box and writes the other computer's address on it, without opening the envelope."
      - when: { at: { letter: internet-out } }
        do:
          - move: { letter: link-out }
          - label: { letter: "Letter in envelope in courier box in lorry" }
          - light: [link-out]
          - say: "The link handler loads the box on a lorry and writes the next stop on it, so three wrappers are closed now."
      - when: { at: { letter: link-out } }
        do:
          - move: { letter: link-in }
          - light: [link-in]
          - say: "The lorry crosses the wire, and the link handler on the other side reads only the lorry label."
      - when: { at: { letter: link-in } }
        do:
          - move: { letter: internet-in }
          - label: { letter: "Letter in envelope in courier box" }
          - light: [internet-in]
          - say: "The link handler takes the lorry off, and the internet handler reads the courier box label and nothing under it."
      - when: { at: { letter: internet-in } }
        do:
          - move: { letter: transport-in }
          - label: { letter: "Letter in envelope" }
          - light: [transport-in]
          - say: "The courier box comes off, and the transport handler reads the envelope label to see which program gets the letter."
      - when: { at: { letter: transport-in } }
        do:
          - move: { letter: app-in }
          - label: { letter: "Letter" }
          - mark: { letter: good }
          - light: [app-in]
          - say: "The envelope comes off, and the server app reads the letter, with each handler reading one label and leaving the rest alone."
      - when: { at: { letter: app-in } }
        do:
          - say: "The letter has arrived. Press Reset to send it again."
  - id: peek
    label: Try to read the envelope label
    cases:
      - when: { at: { letter: app-out } }
        do:
          - say: "No envelope exists yet. The letter has no wrappers."
      - when: { at: { letter: transport-out } }
        do:
          - say: "The transport handler wrote the envelope label a moment ago, so it can read it back."
      - when: { at: { letter: transport-in } }
        do:
          - say: "The transport handler reads the envelope label, which is allowed because the envelope is the outside wrapper at this stop."
      - when: { at: { letter: app-in } }
        do:
          - say: "All wrappers are gone, so there is no envelope label left to read."
    refuse: "The envelope label is under other wrappers at this stop. A handler reads only the label on the outside, so nothing is read."
script: [next, next, next, peek, next, next, next, peek]
try:
  - text: "Press \"Next stop\" three times. Watch the envelope, the courier box and the lorry close around the letter, one at a time."
    done: [next, next, next]
  - text: "Press \"Try to read the envelope label\" while the letter sits on the lorry. The envelope is under two wrappers, so nothing is read."
    done: [peek]
  - text: "Press \"Next stop\" three more times, until the letter is at the transport stop on the receiving side. Then press \"Try to read the envelope label\" again. Now it works."
    done: [next, next, next, peek]
```

**What you should notice:** The wrappers close in one order on the way out and open in the reverse order on the way in, and a handler can read a label only when its wrapper is on the outside.

## Build the logic

### Notice

Your app writes a short request: "Send me picture 7." That letter has no address. It has no way to say which program should get it. It cannot cross a radio link by itself. Something has to add each of these, and the app should not have to know how.

### Guess

How could that letter reach a program on a computer far away? Here are three ideas.

- A. One big handler knows everything: what the app wrote, where the other computer is and how the wire works.
- B. The app writes the address itself and sends the letter straight along the wire.
- C. A chain of handlers. Each does one job, writes its own label and passes the letter on. The other side undoes the chain in reverse.

Idea A would break every app whenever a wire changes. Idea B makes every app learn every wire. Idea C keeps each job small, so a new kind of wire does not touch the apps. Idea C is the answer.

### Build

1. The app writes the letter and hands it down.
2. The transport handler seals it in an envelope. The label says which program on the other computer should get it.
3. The internet handler puts the envelope in a courier box. The label holds the other computer's address.
4. The link handler loads the box on a lorry. The label says the next stop on this stretch of road.
5. The lorry crosses the wire.
6. On the other side, the handlers work in the opposite order. The link handler reads the lorry label and takes the lorry off. Then the internet handler reads the courier box label and takes the box off. Then the transport handler reads the envelope label and takes the envelope off.
7. The server app receives the letter as the app wrote it.
8. A handler never reads under the outside wrapper, and it never rewrites a wrapper that is not its own.

### Name it

A layer is one step in the chain, with one job. A **protocol** is a set of agreed rules that two sides follow. A **header** is the label a layer writes in front of the data. **Encapsulation** is the wrapping of data in a header at each layer on the way out.

This lesson uses the four layers named in RFC 1122, an Internet standards document. The internet layer is sometimes called the network layer, for example in MDN's picture. Another picture, the seven-layer OSI model, splits the same work into more layers. It is general knowledge, and it is not used here. In the table, **HTTP** is the language web programs speak, **TCP** is a protocol that delivers data between two programs, and **IP**, the Internet Protocol, gives each computer an address (general knowledge).

| Layer | Its one job | Example | Picture |
|---|---|---|---|
| Application | The words the two programs speak | HTTP | The letter |
| Transport | Delivery between two programs | TCP | The envelope |
| Internet | The address of the other computer | IP | The courier box |
| Link | The next hop on one network | Wi-Fi, cellular, Ethernet | The lorry |

The rows come from three sources. TCP is a protocol that delivers data between two programs, and TLS is a protocol that encrypts a connection (general knowledge). MDN says HTTP is an application layer protocol that is sent over TCP, or over a TLS-encrypted TCP connection. RFC 1122 says the transport layer provides end-to-end communication services for applications, and that all Internet transport protocols use the Internet Protocol, IP, to carry data from source host to destination host. It says a host must implement the communication protocol used to interface to its network, which is the link layer. That Wi-Fi, cellular and Ethernet are link examples is general knowledge. MDN also says the layered design hides the network and transport layers from HTTP, so HTTP is described without them.

Here is the whole trip as a trace. Reading from top to bottom, a layer adds its wrapper going down and removes it going up.

| Step | Handler | What it does with the letter |
|---|---|---|
| Out 1 | Transport | Adds the envelope |
| Out 2 | Internet | Adds the courier box |
| Out 3 | Link | Adds the lorry |
| Wire | None | The lorry crosses |
| In 1 | Link | Reads the lorry label, takes it off |
| In 2 | Internet | Reads the courier box label, takes it off |
| In 3 | Transport | Reads the envelope label, takes it off |
| End | Server app | Reads the letter |

The statement that each layer reads only its own label is general knowledge. RFC 1122 section 1.1.3 has no sentence that says it in those words.

## Predict the output

```challenge
type: predict
run: false
id: who-reads-the-box-label
verified: not-run
code: |
  A letter is wrapped like this, outside to inside:
    lorry label:        "Link: next stop is the Wi-Fi router"
    courier box label:  "Internet: deliver to computer 203.0.113.9"
    envelope label:     "Transport: give it to the picture app"
    letter:             "Send me picture 7"

  Which handler reads the courier box label?
options:
  - id: a
    text: "The link handler, because the lorry is on the outside"
    why: "You may have picked whoever is first in line. The link handler reads only the lorry label and then takes the lorry off."
  - id: b
    text: "The transport handler, because it is closest to the app"
    why: "You may have thought the handler nearest the app sees everything. The transport handler reads the envelope label, which sits under the box."
  - id: c
    text: "The internet handler, after the link handler has taken the lorry off"
    correct: true
    why: "Each handler reads the label on the outside. Once the lorry is off, the courier box is the outside wrapper, and the internet handler reads its label."
output: |
  The internet handler, after the link handler has taken the lorry off
```

```challenge
type: predict
run: false
id: order-the-wrappers-open
verified: not-run
code: |
  A message goes out through four layers
  and arrives at the server.

  In what order are the wrappers opened on the way in?
options:
  - id: a
    text: "Lorry, courier box, envelope"
    correct: true
    why: "The last wrapper closed is the outside one, so it is the first one opened. The wrappers open in the reverse order of closing."
  - id: b
    text: "Envelope, courier box, lorry"
    why: "You may have opened them in the order they were closed. The lorry was closed last, so it is on the outside and must come off first."
  - id: c
    text: "Any order, because each handler can open any wrapper"
    why: "You may have thought the handlers share the job. A handler reads the outside label only, so the order is fixed."
output: |
  Lorry, courier box, envelope
```

```challenge
type: predict
run: false
id: transport-edits-the-letter
verified: not-run
code: |
  A transport handler decides to change the words
  inside the letter to make it shorter.

  What goes wrong?
options:
  - id: a
    text: "Nothing, because the letter is only words"
    why: "You may have thought any handler may tidy the letter. Each handler writes and reads its own label only."
  - id: b
    text: "The server app reads a different letter from the one your app wrote"
    correct: true
    why: "The envelope is the handler's wrapper. The letter inside belongs to the app. If the handler rewrites it, the app on the other side gets words nobody wrote."
  - id: c
    text: "The courier box label is lost"
    why: "You may have thought the labels are tied together. The box label is a separate wrapper, and the handler's change does not touch it."
output: |
  The server app reads a different letter from the one your app wrote
```

## Tricks to remember

- Wrap on the way out, unwrap on the way in, in the opposite order.
- A handler reads the outside label and nothing else.
- Letter, envelope, courier box, lorry: application, transport, internet, link.
- One job for each layer, so a new wire does not break an app.
- Task: draw your own trace for the request "give me picture 7". Write the four wrappers on the way out and the three openings on the way in.

## Common mistakes

- **Thinking every layer reads the whole message.** The letter is at the middle of every parcel, so it feels as if everyone can see it. Each handler reads one label on the outside.
- **Thinking the layers are separate devices.** The lorry and the courier box feel like different workers. On your phone the layers are pieces of software that take turns, and a router in the middle, a device that passes data from one network to the next, may open only some of the wrappers (general knowledge).
- **Thinking the last layer is the most important one.** The wire is the part you can touch, so it feels like the real network. Each layer does a job the others cannot, so a failure at any one of them stops the message.

## Where this shows up in Android

When a request from an app fails, the error often has a name, and the name points at one step of the trip. These are the descriptions on the Android reference pages for these classes. A socket is one end of a connection between two programs, a port is the number that picks the program on a computer, and a handshake is the first exchange in which the two sides agree how to protect the connection (general knowledge).

- `UnknownHostException`: "Thrown to indicate that the IP address of a host could not be determined." The app could not find the address for the name.
- `ConnectException`: "Signals that an error occurred while attempting to connect a socket to a remote address and port." The page adds that typically the connection was refused remotely, for example when no process is listening on the remote address and port.
- `SSLHandshakeException`: "Indicates that the client and server could not negotiate the desired level of security." The page adds that the connection is no longer usable.

My own reasoning: these three names fit three different steps, finding the address, opening the connection and agreeing on security. Matching them to layers is a rough guide, because looking up a name is done at the application layer (general knowledge) and the security step sits between the application and transport layers. The part of this lesson that explains the pattern is that each step has one handler, and a failure names the handler that could not do its job.

## Say it in an interview

**Why does a network message pass through several layers before it leaves your phone?**
Each layer does one job and hides it from the others. MDN says the layered design hides the network and transport layers, so HTTP can be described without them. The benefit is that a new kind of wire, such as Wi-Fi or cellular, does not force every app to change (general knowledge). The trade-off is a little extra data for each wrapper (my own reasoning).

**What does each layer do, in the four-layer picture?**
The application layer carries the words two programs speak, such as HTTP. The transport layer delivers between two programs, for example with TCP. The internet layer carries data from one computer to another using IP addresses. The link layer moves it across one network, such as Wi-Fi. RFC 1122 names these four layers.

**How do wrapping and unwrapping work as data moves down and up?**
On the way down, each layer adds its own header in front of the data. On the way up, the layers remove their headers in the opposite order. Each layer reads only its own header. That statement is general knowledge, and RFC 1122 does not word it that way.

**What goes wrong when you debug a failed request and do not know which layer failed?**
You may change the wrong thing. A name that cannot be found, a connection that is refused and a security handshake that fails are three different problems, with different error names. My own reasoning: I would read the error name first and ask which step it points at before I change any code.

## Recap

1. Each layer does one job and adds its own label on the way out.
2. On the way in, the labels are removed in the opposite order.
3. A layer reads only its own label and never edits the layer inside.

## You can now

- Explain why a message passes through layers, using the letter, envelope, courier box and lorry.
- Predict which handler reads which label in a drawn example.
- Predict the order in which the wrappers open on the way in.
- Explain what goes wrong when one layer rewrites the message inside it.
