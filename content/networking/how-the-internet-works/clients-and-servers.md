---
id: networking/how-the-internet-works/clients-and-servers
title: When my app loads data, which computer asks and which one answers?
status: shadow
difficulty: easy
order: 1
requires: []
summary: In an app, one computer asks for data and another answers. Your phone asks, a server answers, and the server never starts the talk.
video: ""
publishedOn:
updatedOn:
sims: []
playground: false
fruit: false
upnext: false
---

<!-- ⚠️ VERIFY: Assumes foundation/how-computers-work/what-does-a-computer-do (draft exists, not live). Checked 21 Oct 2026: developer.android.com/training/basics/network-ops/connecting says INTERNET and ACCESS_NETWORK_STATE are normal permissions, granted at install time, not requested at runtime; and that Android throws NetworkOnMainThreadException if you do network work on the main thread. Left out on purpose: how chat messages and push alerts "arrive by themselves" (needs the push-notification docs and a later leaf). The roadmap P item with four situations was cut to three clear ones for the same reason. The claim that an app that never decides to stop waiting keeps its spinner is general logic, not from an official page. Roadmap row says no sim: one still diagram of two boxes and an arrow; that diagram asset does not exist yet. Predict items traced by hand only. The roadmap F item (server function calls client first) was not written because this leaf is playground: false. -->
## The question

You open a weather app. A spinner turns for a second, then today's forecast appears. The forecast was not stored in your phone. It came from somewhere else.

Which computer asked for it, and which computer answered? And what happens to the spinner when nobody answers?

## Mental model

Think of a restaurant.

- You, sitting at the table, are the customer. In the app world, that is your app.
- The waiter carries your order to the kitchen and brings the food back. That is the network.
- The kitchen cooks the order and sends the dish out. That is the server.
- The order is the question you ask. The dish is the answer.

You decide to order. The waiter carries it. The kitchen answers. The kitchen never walks to your table on its own and puts food in front of you.

**Where the analogy stops:** A restaurant has one kitchen and a handful of tables. A real server answers thousands of customers at the same time, and it may be a computer in a building you will never see. Also, a kitchen can be one room, but "customer" and "kitchen" are jobs, not kinds of machine. One computer can be both, depending on the moment.

## Build the logic

### Notice

You pull down on the weather app. The phone sends the words "give me the forecast for my city". A short time later, text and numbers come back. You see them as the forecast.

Now stop the kitchen. Pull down again. The phone sends the same words, and nothing comes back. The spinner keeps turning. The customer is still waiting.

### Guess

Who started the conversation?

- A. The server, because it holds the data.
- B. The phone, because the user wanted something.
- C. Both at the same time, because the data lives in the cloud.

Idea A feels right because the server has what you want. But a kitchen with food and no order cooks nothing. Idea C treats "the cloud" as one big space that holds everything. It is not. It is other computers, with an address, that answer when asked. Idea B matches the restaurant.

### Build

1. Your app decides it needs data and writes a question.
2. The network carries the question to a computer that holds the data.
3. That computer reads the question, finds the answer and writes it down.
4. The network carries the answer back.
5. Your app shows it, and the spinner stops.

If step 3 never happens, the app is stuck at step 2 waiting. An app has to decide how long it will wait.

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

One rule to keep: the client starts, the server answers. The kitchen never walks to your table.

## Predict the output

```challenge
type: predict
run: false
id: who-asks-first
verified: not-run
code: |
  Three situations. In each, which side sends the first message?
    1. You open a web page in your browser.
    2. You press Upload on a photo.
    3. You pull down to refresh the weather.
  Pick the answer.
options:
  - id: a
    text: "The server sends first in all three"
    why: "You may have thought the side with the data goes first. A kitchen with no order has nothing to cook. The server waits."
  - id: b
    text: "The client sends first in all three"
    correct: true
    why: "In each case you did something and the app wrote an order. The waiter carries it to the kitchen. The kitchen only answers."
  - id: c
    text: "The client in 1 and 3, the server in 2"
    why: "You may have thought that uploading means the server takes the photo. The server cannot take what nobody sent. Your phone starts by sending the photo."
output: "The client sends first in all three"
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
    why: "You may have expected the phone to know. The phone only knows the kitchen is closed once it hears nothing back, and nothing tells it that. It keeps waiting."
  - id: b
    text: "The spinner keeps turning until the app gives up waiting"
    correct: true
    why: "The order was sent, but the kitchen is closed. Nobody answers, so the customer keeps waiting until the app decides to stop."
  - id: c
    text: "The forecast appears anyway, from the phone's own cloud"
    why: "You may have pictured data living in a cloud near the phone. It lives on other computers. If they are off, nothing arrives."
output: "The spinner keeps turning until the app gives up waiting"
```

## Tricks to remember

- Client asks. Server answers. In that order, every time.
- The kitchen never walks to your table.
- "The cloud" is somebody else's computer that answers when asked.
- A spinner that never ends means an order was sent and nothing came back.

## Common mistakes

- **Thinking the phone is wired straight to the data.** It feels that way because the forecast appears fast. Between your phone and the data there is a network and a server. Ask which of the three, app, network or server, is slow.
- **Thinking "client" means a phone and "server" means a big machine.** Most examples use a phone and a big computer, so the words stick to them. They are jobs. A laptop can be a client, and a phone could answer requests too.
- **Thinking messages that arrive by themselves prove the server starts the talk.** A chat message can pop up without you doing anything, so it looks like the kitchen walked to your table. How that works takes a later lesson. For now, keep the rule for loading data: the app asks first.

## Where this shows up in Android

The spinner that never ends is the symptom to look for. An app on Android is a client. It sends a request over the network and waits for a response. If the server is down or the network is cut, the response never comes. An app that does not decide when to stop waiting will leave the spinner on the screen.

Two facts from the Android documentation fit this picture. First, an app has to declare the INTERNET permission to reach the network. The page calls it a normal permission, which means it is granted when the app is installed and the user is not asked while the app runs. Second, Android does not allow network work on the main thread. It throws a NetworkOnMainThreadException if you try. Both rules come from the same idea: the order has to go out to another computer, and you have to wait for it without freezing the screen.

## Recap

1. A client asks, a server answers, and the server never starts the conversation.
2. A request goes out, a response comes back, and the network carries both.
3. A spinner that never stops means a request went out and no response came back.

## You can now

- Explain client, server, request and response using the restaurant.
- Say which side starts the conversation in a simple situation, like opening a page or uploading a photo.
- Predict what the app shows when the server stops answering.
