---
id: networking/how-the-internet-works/how-data-travels-in-packets
title: How does a message cross the internet as many small packets?
status: live
difficulty: easy
order: 2
requires: [networking/how-the-internet-works/clients-and-servers]
summary: A big message is cut into numbered packets that can take different routes. The receiver puts them back in order.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [postcard-routes]
playground: false
fruit: false
upnext: false
---


## The question

You open a photo on a slow connection and it fills in bit by bit. Later a video stops with a spinner and then carries on. The data did arrive in the end. How does a big message get across the internet? Is it one piece sliding down one wire?

## Mental model

Think of a book posted as numbered postcards.

- The book is the message.
- Each postcard carries a few lines of it. That is a **packet**.
- Each postcard has the sender, the address, "card 3 of 5" written on it.
- The postal system sends each card on its own. Cards can take different routes.
- Some cards arrive late, and one may never arrive.
- The reader puts the cards in order by their numbers.

The book is whole only when all five numbers have turned up.

**Where the analogy stops:** The post office never asks you for a missing card. On the internet, with a protocol such as TCP, the receiving side notices a gap and asks for that one packet again. Also, a packet carries bytes, not lines of a book, and the receiving computer joins the bytes back into the message. Finally, your app code normally never sees the packets at all. The operating system's network code does the joining (general knowledge).

## See it move

<!-- SIM: postcard-routes -->

**Try this:**
1. Send the book. Watch which route each numbered card takes and read the order the cards arrive in.
2. Close one route and send again. Watch the cards pick the other route.
3. Drop card 3 and watch the receiver's tray after the cards have arrived.

**What you should notice:** The cards can arrive in a different order than they were sent, and a missing number is the only thing that has to be sent again.

## Build the logic

### Notice

A book of 5 pages is sent as 5 postcards, numbered 1 to 5. Each card also says "of 5". The cards arrive in this order: 2, 1, 4, 5. Card 3 has not arrived.

### Guess

What should the receiver do?

- A. Put the cards in order by their numbers, notice that 3 is missing and ask for card 3 only.
- B. Read the cards in the order they arrived, then ask for nothing.
- C. Throw everything away and ask for the whole book again.

Idea A is the answer. Idea B would give you pages in the wrong order. Idea C works, but it wastes the four good cards. The MDN page says it is quicker and easier to request the missing packets than the entire file.

### Build

1. Cut the message into pieces small enough to send.
2. Write a header on each piece: who it is from, who it is for, its number and how many pieces there are in total.
3. Send every piece out on its own. Different pieces can take different paths.
4. At the other end, collect the pieces as they arrive. Do not trust the arrival order.
5. Sort the pieces by number.
6. If a number never shows up, ask for that piece again.
7. When all the numbers are present, join the pieces into the message.

### Name it

A packet is a small chunk of data sent across a network. The MDN page says each packet has a **header** and a **payload**. The header holds details such as the sender and receiver addresses, the packet number, the total number of packets and details of the protocols used. The payload is the actual data. A **protocol** is an agreed set of rules for sending messages.

A **router** is a device that passes packets on towards where they are going (general knowledge). A router keeps packets waiting in a **queue**, which is a line where the first packet in is the first one out. If the line is full, the router can drop a packet (general knowledge). That is one way a packet goes missing.

The MDN page gives two reasons for sending packets. A packet that is dropped or corrupted can be asked for again without resending the whole file. And packets can travel along different paths, which keeps the network efficient when many people ask for the same thing. The packets may arrive out of order, and the receiver uses the headers to put them right.

The MDN picture is simplified. Real headers differ by protocol (general knowledge). The official specification of TCP (called RFC 9293) says that TCP, a protocol that carries much app data (general knowledge), provides a reliable, in-order, byte-stream service to applications (a stream is data that arrives as one ordered flow), and that it detects lost packets using sequence numbers. So your app gets an ordered stream, and the repair work happens below it.

The code below is a toy model of the receiver. A **data class** is a Kotlin class that holds values, such as `Packet`. The call `chunked(4)` cuts the text into pieces of 4 characters. The call `mapIndexed` builds one value for each piece and gives you its position `i` starting at 0. The call `sortedBy` orders the list by a value. The call `joinToString("")` glues the pieces together with nothing between them.

```kotlin
data class Packet(val number: Int, val total: Int, val payload: String)

fun main() {
    val pieces = "HELLO WORLD!".chunked(4)
    val packets = pieces.mapIndexed { i, text -> Packet(i + 1, pieces.size, text) }
    val arrived = listOf(packets[2], packets[0], packets[1])
    println(arrived.map { it.number })
    println(arrived.sortedBy { it.number }.joinToString("") { it.payload })
}
```

This prints `[3, 1, 2]`, then `HELLO WORLD!`. The text splits into `HELL`, `O WO` and `RLD!`. The packets arrive as 3, 1, 2. Sorting by number puts them back as 1, 2, 3, and the message comes out whole. This is a toy. It does not model routes, delays or lost packets.

| Postcards | Official name | Phone example |
|---|---|---|
| A card | Packet | A small chunk of a photo |
| The "card 3 of 5" note | Header | Sender, receiver, number, total |
| The lines of the book | Payload | The data itself |
| The post depot | Router | A device on the path |

## Predict the output

```challenge
type: predict
run: false
id: which-card-arrives-first
verified: not-run
code: |
  Two routes: Route A takes 2 minutes. Route B takes 5 minutes.
  One card is sent each minute:
    card 1 at minute 0, on Route B   (arrives at minute 5)
    card 2 at minute 1, on Route A   (arrives at minute 3)
    card 3 at minute 2, on Route A   (arrives at minute 4)
    card 4 at minute 3, on Route B   (arrives at minute 8)
  In what order do the cards arrive?
options:
  - id: a
    text: "1, 2, 3, 4"
    why: "You may have used the sending order. The cards take different times, so the arrival order is not the sending order."
  - id: b
    text: "2, 3, 1, 4"
    correct: true
    why: "The arrival minutes are 3, 4, 5 and 8. Card 1 was sent first, but the slow route made it arrive third. The numbers on the cards are what fix this."
  - id: c
    text: "2, 1, 3, 4"
    why: "You may have placed card 1 early because it was sent first. It arrives at minute 5, after cards 2 and 3."
output: |
  2, 3, 1, 4
```

```challenge
type: predict
run: false
id: join-by-number
verified: not-run
code: |
  fun main() {
      val arrived = listOf("C" to 3, "A" to 1, "B" to 2)
      println(arrived.joinToString("") { it.first })
      println(arrived.sortedBy { it.second }.joinToString("") { it.first })
  }
options:
  - id: a
    text: "CAB, then ABC"
    correct: true
    why: "The first line joins in arrival order: C, A, B. The second line sorts by the number on each card first, so the message reads A, B, C."
  - id: b
    text: "ABC, then ABC"
    why: "You may have expected the first line to be right as well. Without sorting, the cards are joined in the order they arrived."
  - id: c
    text: "CAB, then CAB"
    why: "You may have thought sorting changes nothing. sortedBy gives a new list ordered by the number, so the second line comes out in order."
output: |
  CAB
  ABC
```

```challenge
type: predict
run: false
id: card-three-is-lost
verified: not-run
code: |
  A message was cut into 5 packets. Each header says "of 5".
  Packets 1, 2, 4 and 5 arrive. Packet 3 does not.
  What does the receiver know, and what does it do?
options:
  - id: a
    text: "It joins 1, 2, 4 and 5 and shows the message as it is"
    why: "You may have trusted whatever arrived. The header says 'of 5', so the receiver knows four cards cannot be the whole book."
  - id: b
    text: "It asks for all five packets again"
    why: "You may have thought a gap spoils everything. That works, but the MDN page says it is quicker to ask for the missing packets only."
  - id: c
    text: "It sees that number 3 is missing and asks for packet 3 only"
    correct: true
    why: "The numbers and the total tell the receiver exactly which card is absent. The four good cards stay where they are."
output: |
  It sees that number 3 is missing and asks for packet 3 only
```

## Tricks to remember

- Cut it, number it, send each piece alone, sort it, mend the gaps.
- Arrival order is not sending order. The numbers decide.
- A lost packet costs one packet, not the whole message.
- Optional: if you have a packet capture tool such as Wireshark on a computer, record a page load and look at the header of one packet. This is not needed for the lesson.

## Common mistakes

- **Thinking a message crosses the internet as one piece on one fixed wire.** The answer arrives as one thing, so it feels like one thing. It is many numbered packets that can take different paths.
- **Thinking every packet takes a different path.** The postcard picture makes it sound that way. Packets can take different paths, and many take the same one.
- **Thinking one lost packet loses the whole message.** A gap looks like a failure. The receiver can see which number is missing and ask for that one only.
- **Thinking your app sees the packets.** The network code in the operating system joins them. Your app gets a stream in order (general knowledge, and the TCP specification).

## Where this shows up in Android

Take the video that stops and shows a spinner. The Android documentation for the Media3 player says its buffering state means the player is not able to play from its current position right now, mostly because more data needs to be loaded. My own reasoning: the player had played everything it had and was waiting for more data to arrive.

Take the photo that fills in bit by bit. This part is my own reasoning, and I did not find an Android page that says it. A picture file arrives in pieces, so on a slow connection the app can draw what has arrived so far. Both symptoms come from the same fact: the data arrives in pieces over time, not as one block.

## Say it in an interview

**Why does the internet break messages into packets instead of sending a whole file at once?**
Two reasons, both from the MDN page. If a packet is dropped or damaged, only that packet needs sending again. And packets can take different paths, which spreads the load and keeps the network efficient when many people ask for the same thing. The trade-off is that the receiver has to put the pieces back in order.

**What is in a packet header?**
The MDN page lists the sender and receiver addresses, the packet number, the total number of packets and details of the protocols used. That is a simplified picture, and real headers differ by protocol. The payload is the data itself.

**What happens when a packet is lost?**
With TCP, the receiver sees a gap in the numbers and the missing piece is sent again. The TCP specification says TCP detects packet loss with sequence numbers, so the app does not see the gap. The cost is a pause, because the data after the gap cannot be handed on until the gap is filled (my own reasoning from the in-order promise).

**Packets can arrive out of order. Who fixes that, and does the app see it?**
The receiving side uses the packet numbers to sort them. For TCP, the specification says the service to applications is in-order, so the app gets a clean stream. If you read a stream and the data looks complete, this repair has already happened below your code.

## Recap

1. A big message is cut into small numbered packets with headers.
2. Packets can take different routes and arrive in a different order.
3. The receiver sorts by number and asks again only for what is missing.

## You can now

- Work out the order packets arrive in from a map with two routes.
- Explain why a lost packet costs one packet and not the whole message.
- Say what a header holds and why the packet number matters.
