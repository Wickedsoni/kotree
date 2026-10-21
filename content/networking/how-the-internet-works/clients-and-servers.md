---
id: networking/how-the-internet-works/clients-and-servers
title: When my app loads data, which computer asks and which one answers?
status: live
difficulty: easy
order: 1
requires: []
summary: When an app loads data, the app asks and a server answers. See who does what, and what you see when nobody answers.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: []
playground: false
fruit: false
upnext: false
---

## The question

You open a weather app. A little circle turns for a second, and then today's forecast appears. The forecast was not stored on your phone. Which computer asked for it, and which computer answered? And what happens to the little circle when nobody answers?

## Mental model

Think of a restaurant.

- You, sitting at the table, are the customer. In the app world, that is your app.
- The order is the question you ask: "one soup, please".
- The waiter carries the order to the kitchen and brings the food back. That is the network, the wires and radio links that carry messages between computers.
- The kitchen cooks and sends the food out. That is the computer that holds the data.
- The dish is the answer.

You decide to order. The waiter carries it. The kitchen answers. The kitchen does not walk to your table on its own and put food in front of you.

**Where the analogy stops:** A restaurant has one kitchen and a few tables. A real data computer answers thousands of customers at the same moment (general knowledge), and it may sit in a building you will never see. Also, "customer" and "kitchen" are jobs, not kinds of machine. One computer can be the customer in one moment and the kitchen in the next.

## Build the logic

### Notice

You pull down on the weather app. The phone sends the words "give me the forecast for my city". A moment later, text and numbers come back, and you see them as the forecast.

Now close the kitchen. Pull down again. The phone sends the same words, and nothing comes back. The little circle keeps turning. The customer is still waiting.

### Guess

Who started the conversation?

- A. The phone, because you wanted something.
- B. The computer that holds the data, because it has what you want.
- C. Both at the same time, because the data sits in "the cloud".

Idea B feels right because that computer has the data. But a kitchen with food and no order cooks nothing. Idea C treats "the cloud" as one big space that holds everything. The word is a loose name for computers owned by someone else, and they answer when asked (general knowledge). Idea A matches the restaurant.

### Build

1. Your app decides it needs data and writes a question.
2. The network carries the question to a computer that holds the data.
3. That computer reads the question, finds the answer and writes it down.
4. The network carries the answer back.
5. Your app shows the answer, and the little circle stops.

If step 3 never happens, the app is stuck waiting after step 2. This next part is my own reasoning: so the app needs a rule for how long it will wait before it gives up.

### Name it

- A **client** is the computer, or the program, that asks. Your app is a client.
- A **server** is the computer, or the program, that listens and answers. It waits until a question comes.
- A **request** is the question the client sends.
- A **response** is the answer the server sends back.

| Restaurant | Official name | Weather app example |
|---|---|---|
| The customer | Client | Your app on the phone |
| The order | Request | "Give me the forecast for my city" |
| The kitchen | Server | A computer run by the weather company |
| The dish | Response | The forecast numbers |

One rule to keep when an app loads data: the client asks first, and the server answers. There is no program to run on this page. It is a picture of who does what.

## Predict the output

```challenge
type: predict
run: false
id: who-asks-first
verified: not-run
code: |
  Three situations. In each one, which side sends the first message?
    1. You open a web page in your browser.
    2. You press Upload on a photo.
    3. You pull down to refresh the weather.
options:
  - id: a
    text: "The client sends first in all three"
    correct: true
    why: "In each case you did something, and your device wrote an order. The waiter carries it to the kitchen, and the kitchen answers."
  - id: b
    text: "The server sends first in all three"
    why: "You may have thought the side with the data goes first. A kitchen with no order has nothing to cook, so it waits."
  - id: c
    text: "The client in 1 and 3, the server in 2"
    why: "You may have thought an upload means the server fetches the photo. The server cannot take what nobody sent. Your phone starts by sending the photo."
output: |
  The client sends first in all three
```

```challenge
type: predict
run: false
id: stop-the-kitchen
verified: not-run
code: |
  The weather company's server is switched off.
  You pull down to refresh the weather.
  The phone sends the request.
  What do you see?
options:
  - id: a
    text: "The old forecast turns into an error message by itself"
    why: "You may have expected the phone to know. Nothing arrives to tell it, so from the app's side the answer is only late."
  - id: b
    text: "The forecast appears anyway, from a cloud near the phone"
    why: "You may have pictured the data living close by. It lives on other computers. If they are off, nothing arrives."
  - id: c
    text: "The little circle keeps turning until the app gives up waiting"
    correct: true
    why: "The order was sent, but the kitchen is closed. Nobody answers, so the customer keeps waiting until the app decides to stop."
output: |
  The little circle keeps turning until the app gives up waiting
```

## Tricks to remember

- Client asks, server answers, in that order, when an app loads data.
- The kitchen does not walk to your table.
- "The cloud" means other people's computers that answer when asked.
- A circle that never stops turning means an order went out and no dish came back.

## Common mistakes

- **Thinking the phone is wired straight to the data.** It feels that way because the forecast appears fast. Between your phone and the data there is a network and a server. Ask which of the three, app, network or server, is slow.
- **Thinking "client" means a phone and "server" means a big machine.** Most examples use a phone and a big computer, so the words stick to them. They are jobs. A laptop can be a client, and a phone could answer requests too.
- **Thinking a message that pops up by itself proves the server starts every conversation.** A chat message can appear without you touching anything, so it looks like the kitchen walked to your table. This page is about loading data, where your app asks first. Keep that rule for loading data only.

## Where this shows up in Android

The symptom is the spinner (the little turning circle) that never stops. An Android app is a client. It sends a request over the network and waits for a response. If the server is down or the network is cut, no response comes, and an app that never stops waiting leaves the spinner on the screen (my own reasoning). The Android documentation lists configurable timeouts, which are limits on how long the app waits, as a feature of its `HttpsURLConnection` client.

Two more facts from the same Android page fit the picture. First, the app lists the `INTERNET` permission in its manifest, the file where an app declares what it needs. The page calls it a normal permission, which means it is granted when the app is installed and the app does not ask the user while it runs. Second, the page says that if you do network work on the main thread, Android throws a `NetworkOnMainThreadException`. The main thread is the part of the app that draws the screen and reacts to taps (general knowledge), so a wait there would freeze the screen. The order has to go out to another computer, and the app must wait for it without freezing.

## Say it in an interview

**When your mobile app requests data, which computer is the client and which is the server?**
The app on the phone is the client, because it starts the request. The computer that holds the data and answers is the server. Client and server are jobs, not kinds of machine.

**What is the request and response cycle?**
The client sends a request, which is a question. The server reads it and sends back a response, which is the answer. The network carries both. If no response comes, the client is left waiting, so it needs a limit on how long it waits.

**Why does an app fetch data from a server instead of keeping everything on the phone?**
This answer is general knowledge and my own reasoning. Data that many users share, such as a forecast, lives in one place so everyone sees the same answer. The phone also has little room. The trade-off is that the app now depends on the network and the server being there.

**What goes wrong if an app assumes the server is always available?**
The request goes out and no answer returns, so the screen shows a spinner that never ends. The Android documentation says its client supports configurable timeouts, so the app can stop waiting. A saved copy of the last answer on the phone, called a cache, can fill the screen meanwhile (general knowledge). A good answer also says what the user sees while the app waits.

## Recap

1. When an app loads data, the client asks and the server answers.
2. A request goes out, a response comes back, and the network carries both.
3. A spinner that never stops means a request went out and no response came back.

## You can now

- Say which side sends the first message in a simple situation, such as opening a page or uploading a photo.
- Predict what the app shows when the server stops answering.
- Explain why a circle that never stops turning means no response came back.
