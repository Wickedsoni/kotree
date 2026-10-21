---
id: foundation/how-computers-work/memory-vs-storage
title: Why does a phone have both memory and storage?
status: live
difficulty: easy
order: 5
requires: [foundation/how-computers-work/what-is-a-bit]
summary: Memory is the fast place a phone works in right now. Storage is the slower place where files stay when the power goes off.
video: ""
publishedOn: 2026-10-20
updatedOn: 2026-10-20
sims: [desk-and-cabinet]
playground: false
fruit: false
upnext: false
---

## The question

You are typing a long message on your phone. The battery dies before you press Send. You charge the phone and open the app. The message is gone. But the photos you took last week are all still there.

Why does one thing vanish while the other stays? And why does a phone keep things in two different places?

## Mental model

Think of a desk and a filing cabinet in a small office.

- The desk is where you work right now. You can only work on papers that are lying on it.
- The papers on the desk are what the phone is using at this moment: the app you have open, the message you are typing.
- The filing cabinet is where papers wait until you need them. It holds far more than the desk, but you have to walk over and open a drawer.
- Every night a cleaner clears the whole desk. The cabinet is left alone. The cleaner stands for the power going off.

**Where the analogy stops:** A cleaner comes at night, and you know it. On a phone the desk is cleared the moment the power goes, with no warning. Also, when you file a real paper, it leaves the desk. On a phone, saving makes a copy, so the same data can sit in both places at once.

## See it move

```sim
id: desk-and-cabinet
template: zones
title: Desk, filing cabinet and the power plug
intro: A typed message that is not saved lies on the desk. The cabinet holds three saved papers. The desk has room for two papers.
zones:
  - { id: desk, label: Desk (memory) }
  - { id: cabinet, label: Filing cabinet (storage) }
items:
  - { id: draft, label: Typed message (not saved), at: desk }
  - { id: photo, label: Photo, at: cabinet }
  - { id: song, label: Song, at: cabinet }
  - { id: notes, label: Notes, at: cabinet }
vars: { onDesk: 1 }
show:
  - { var: onDesk, label: "Papers on the desk (room for 2)" }
actions:
  - id: unplug
    label: Pull the power plug
    cases:
      - when: { at: { photo-open: desk } }
        do:
          - remove: draft
          - remove: photo-open
          - set: { onDesk: 0 }
          - say: "The desk is empty and the cabinet is unchanged. The typed message was only on the desk, so it is gone unless a saved copy is in the cabinet."
      - when: { at: { song-open: desk } }
        do:
          - remove: draft
          - remove: song-open
          - set: { onDesk: 0 }
          - say: "The desk is empty and the cabinet is unchanged. The typed message was only on the desk, so it is gone unless a saved copy is in the cabinet."
      - when: { at: { notes-open: desk } }
        do:
          - remove: draft
          - remove: notes-open
          - set: { onDesk: 0 }
          - say: "The desk is empty and the cabinet is unchanged. The typed message was only on the desk, so it is gone unless a saved copy is in the cabinet."
      - do:
          - remove: draft
          - set: { onDesk: 0 }
          - say: "The desk is empty and the cabinet is unchanged. The typed message was only on the desk, so it is gone unless a saved copy is in the cabinet."
    refuse: "The plug was already pulled in this run and the desk was cleared. Press Reset to start again."
drops:
  - item: draft
    to: cabinet
    when: { at: { draft: desk } }
    do:
      - spawn: { id: draft-saved, label: "Typed message (saved copy)", at: cabinet }
      - move: { draft: desk }
      - say: "Saving makes a copy. The message stays on the desk and a saved copy is now in the cabinet."
    refuse: "The typed message is no longer on the desk."
  - item: photo
    to: desk
    when: { at: { photo: cabinet } }
    unless: { var: { onDesk: 2 } }
    do:
      - spawn: { id: photo-open, label: "Photo (open copy)", at: desk }
      - move: { photo: cabinet }
      - add: { onDesk: 1 }
      - say: "The phone loads a copy of the photo onto the desk to work on. The photo is still in the cabinet."
    refuse: "The desk is full, so nothing more fits. Put an open paper back in the cabinet first."
  - item: song
    to: desk
    when: { at: { song: cabinet } }
    unless: { var: { onDesk: 2 } }
    do:
      - spawn: { id: song-open, label: "Song (open copy)", at: desk }
      - move: { song: cabinet }
      - add: { onDesk: 1 }
      - say: "The phone loads a copy of the song onto the desk to work on. The song is still in the cabinet."
    refuse: "The desk is full, so nothing more fits. Put an open paper back in the cabinet first."
  - item: notes
    to: desk
    when: { at: { notes: cabinet } }
    unless: { var: { onDesk: 2 } }
    do:
      - spawn: { id: notes-open, label: "Notes (open copy)", at: desk }
      - move: { notes: cabinet }
      - add: { onDesk: 1 }
      - say: "The phone loads a copy of the notes onto the desk to work on. The notes are still in the cabinet."
    refuse: "The desk is full, so nothing more fits. Put an open paper back in the cabinet first."
  - item: photo-open
    to: cabinet
    when: { at: { photo-open: desk } }
    do:
      - remove: photo-open
      - add: { onDesk: -1 }
      - say: "The open copy of the photo goes back. The desk has more room."
  - item: song-open
    to: cabinet
    when: { at: { song-open: desk } }
    do:
      - remove: song-open
      - add: { onDesk: -1 }
      - say: "The open copy of the song goes back. The desk has more room."
  - item: notes-open
    to: cabinet
    when: { at: { notes-open: desk } }
    do:
      - remove: notes-open
      - add: { onDesk: -1 }
      - say: "The open copy of the notes goes back. The desk has more room."
script: ["photo>desk", "photo-open>cabinet", unplug, "photo>desk", "song>desk", "notes>desk"]
try:
  - text: "Drag a paper from the cabinet onto the desk. Drag it back."
    done: ["photo>desk", "photo-open>cabinet"]
  - text: "Pull the power plug. Look at the desk and the cabinet."
    done: [unplug]
  - text: "Put more papers on the desk than fit, and watch what happens at the edge."
    done: ["photo>desk", "song>desk", "notes>desk"]
```

**What you should notice:** After the plug is pulled the desk is empty and the cabinet is unchanged, so a paper that was never copied into the cabinet is gone.

## Build the logic

### Notice

Mia is writing a note. She has typed three lines and has not saved them. Some apps save for you as you type. This one does not. Her photo of the dog was saved last week.

The battery dies. She charges the phone and opens the note. The dog photo is there. The three lines are not.

### Guess

Why would a phone have two places to keep things? Here are three ideas.

- A. They are two names for the same place.
- B. One place is quick but forgets when the power goes off. The other is slower but remembers.
- C. One place is for apps and the other is for photos.

Idea A would make one place enough, and Mia's note shows the two behave differently. Idea C feels right because apps and photos are different things. But apps are kept in the cabinet too, and a photo is copied onto the desk while you look at it. Idea B is the answer.

### Build

1. Working on something needs a place that is quick to reach. That is the desk.
2. A quick place has to be kept powered. When the power stops, it empties.
3. Things that must last need a place that holds on to them with no power. That is the cabinet. It is slower and has more room.
4. To use a file, the phone copies it from the cabinet to the desk, works on it there, and copies the changes back.
5. If nobody copies the changes back, they live only on the desk. They go when the power goes.
6. The desk has a limit. When it is full, something has to leave it.

### Name it

The desk is **memory**, also called **RAM**, short for random-access memory. It is the fast, short-term place a device works in, and it loses what it holds when the power goes off.

The cabinet is **storage**. It is the place files are kept for the long term. It keeps them with no power, is slower than memory, and usually has more room.

Copying work from memory into storage is to **save**. Copying a file from storage into memory so you can use it is to **load**.

| Office | Official name | Keeps data with no power? | Speed | Room | Phone example |
|---|---|---|---|---|---|
| Desk | Memory (RAM) | No | Fast | Less | The message you are typing, the app that is open |
| Filing cabinet | Storage | Yes | Slower | More | Photos, installed apps, saved files |

## Predict the output

```challenge
type: predict
run: false
id: pull-the-plug
verified: not-run
code: |
  Desk (memory):      paper A, paper B, paper C
  Cabinet (storage):  paper C (a saved copy), paper X
  The power is pulled, then it comes back.
  What is on the desk and in the cabinet now?
options:
  - id: a
    text: "Desk: A, B, C. Cabinet: C, X"
    why: "You may have thought the desk keeps its papers. The cleaner clears the desk the moment the power goes."
  - id: b
    text: "Desk: empty. Cabinet: C, X"
    correct: true
    why: "The desk forgets everything when the power goes. The cabinet keeps its files, and C survives only because a copy had been filed there."
  - id: c
    text: "Desk: empty. Cabinet: empty"
    why: "You may have thought a power cut clears everything. The cabinet holds its files with no power."
output: "Desk: empty. Cabinet: C, X"
```

```challenge
type: predict
run: false
id: desk-is-full
verified: not-run
code: |
  A phone has four apps open, and its desk (memory) is nearly full.
  You open a fifth app.
  Android's documentation says that as memory runs low,
  the system can end apps that are no longer on the screen.
  What happens?
options:
  - id: a
    text: "The fifth app refuses to open"
    why: "You may have expected a full desk to lock the door. The system makes room instead."
  - id: b
    text: "The system clears a background app off the desk to make room"
    correct: true
    why: "The desk has a limit. To fit the new paper, the system takes away one that is not being looked at."
  - id: c
    text: "All four open apps are filed in the cabinet first, so nothing is lost"
    why: "You may have hoped the phone files everything for you. An app that is cleared off the desk does not always get a chance to save."
output: "The system clears a background app off the desk to make room"
```

```challenge
type: predict
run: false
id: game-while-off
verified: not-run
code: |
  You install a game on your phone.
  Then you switch the phone off for the night.
  Where is the game's data while the phone is off?
options:
  - id: a
    text: "On the desk (memory)"
    why: "You may have thought the game stays ready on the desk. The desk is cleared when the power goes."
  - id: b
    text: "In the filing cabinet (storage)"
    correct: true
    why: "Anything that must last while the power is off is kept in the cabinet. The game is copied onto the desk when you open it."
  - id: c
    text: "Nowhere, the phone downloads it again each morning"
    why: "You may have thought a switched-off phone forgets everything. The cabinet keeps the files, so no download is needed."
output: "In the filing cabinet (storage)"
```

## Tricks to remember

- The desk is quick but forgets. The cabinet is slow but remembers.
- Power off clears the desk and leaves the cabinet alone.
- To save is to walk to the cabinet. If you did not walk there, the work is only on the desk.

## Common mistakes

- **Treating memory and storage as the same thing.** Both are counted in GB, and people often say "memory" when they mean storage. So more GB feels like more GB. Ask one question instead: does it keep data with no power? On this page, memory always means the desk.
- **Thinking an open app has saved its work.** The words are on the screen, so they feel safe. But the screen shows the desk. Look for a Save step, or an app that says it saves for you.
- **Thinking saving moves the work off the desk.** A real paper leaves the desk when it is filed. On a phone, saving makes a copy, so the same data can sit in both places.

## Where this shows up in Android

Picture a form you are filling in. You switch to the camera to scan something, then come back. The form is empty.

While you were away, the form lived on the desk. The Android documentation says that when the system runs low on memory, it ends the apps it was keeping out of sight.

Android's own notes on this say the app is ended at once, often without its normal save steps, so progress can be lost.

Storage has a similar rule. The Android documentation says that when the user uninstalls an app, the files saved in the app's own storage are removed. So a level the game keeps in its own storage survives a power cut, but not an uninstall.

## Recap

1. Memory is the fast desk, and it forgets everything when the power goes off.
2. Storage is the filing cabinet: slower, roomier, and it keeps files without power.
3. Saving copies work from the desk to the cabinet, so unsaved work lives only on the desk.

## You can now

- Explain why unsaved work disappears when the battery dies but photos stay, using the desk and the cabinet.
- Predict what is left in memory and in storage after the power is pulled.
- Sort an installed game and an unsaved message into memory or storage.
