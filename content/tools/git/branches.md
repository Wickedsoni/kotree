---
id: tools/git/branches
title: "How can I try an idea without breaking my working code?"
status: live
difficulty: medium
order: 2
requires: [tools/git/what-is-a-commit]
summary: A branch is a movable label on one snapshot, so you can try an idea on a new label while the label for your working code stays where it is.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [branch-labels]
playground: false
fruit: false
upnext: false
---


## The question

You want to try a new idea, such as a different layout for a screen. You are not sure it will work. If it fails, you want your working code to be exactly as it was. How can you try the idea without risking the code that works?

## Mental model

Think of the photo album from the last lesson.

- The album is your history. Each photo is a snapshot, and they sit in a row.
- A sticky note is a branch. It is a small label stuck on one photo. You can peel it off and stick it somewhere else.
- The note called `main` marks the photo that holds your working code. This is your main line. (In your repository it may be called `master`. See Name it.)
- Your thumb is the place you are working from. It rests on one note. In Git this is called HEAD.
- Your room is the working folder. It shows what the photo under your thumb's note looks like.

A second note does not make a second album. It is only another label on a photo. When you take a new photo, it goes after the photo under your thumb's note, and only that note slides forward to the new photo. The other notes stay where they were.

**Where the analogy stops:** A sticky note is moved by hand. A branch label moves itself when you take a snapshot while your thumb is on it. Also, when you move your thumb to another note, the room changes to match that photo, and an album page does not do that.
## See it move

```sim
id: branch-labels
template: lanes
title: "Snapshots on a string and labels you can move"
intro: "Three snapshots hang on a string. The label main sits under snapshot 3 and your thumb is on main. The working folder matches snapshot 3."
lanes:
  - { id: "beads", label: "Snapshots (beads on a string)", kind: "row", slots: 6, index0: false }
  - { id: "mainl", label: "Label main (sits under one snapshot)", kind: "row", slots: 6, index0: false }
  - { id: "tryl", label: "Label try-layout (sits under one snapshot)", kind: "row", slots: 6, index0: false }
items:
  - { id: "b1", label: "Snapshot 1", at: "beads.0" }
  - { id: "b2", label: "Snapshot 2", at: "beads.1" }
  - { id: "b3", label: "Snapshot 3", at: "beads.2" }
  - { id: "main", label: "main (thumb)", at: "mainl.2" }
vars: { held: "main", beads: 3, made: 0, folder: "the snapshot under main" }
show:
  - { var: "folder", label: "Working folder shows" }
actions:
  - id: new-label
    label: "Tie a new label try-layout on this snapshot"
    cases:
      - when: { var: { made: 0, beads: 3 } }
        do:
          - spawn: { id: "try", label: "try-layout", at: "tryl.2" }
          - set: { made: 1 }
          - say: "A new label, try-layout, is tied on the same snapshot as main. Nothing else changes and your thumb is still on main."
      - when: { var: { made: 0, beads: 4 } }
        do:
          - spawn: { id: "try", label: "try-layout", at: "tryl.3" }
          - set: { made: 1 }
          - say: "A new label, try-layout, is tied on the same snapshot as main. Nothing else changes and your thumb is still on main."
      - when: { var: { made: 0, beads: 5 } }
        do:
          - spawn: { id: "try", label: "try-layout", at: "tryl.4" }
          - set: { made: 1 }
          - say: "A new label, try-layout, is tied on the same snapshot as main. Nothing else changes and your thumb is still on main."
      - when: { var: { made: 0, beads: 6 } }
        do:
          - spawn: { id: "try", label: "try-layout", at: "tryl.5" }
          - set: { made: 1 }
          - say: "A new label, try-layout, is tied on the same snapshot as main. Nothing else changes and your thumb is still on main."
    refuse: "The label try-layout is already tied."
  - id: switch-try
    label: "Switch to try-layout"
    cases:
      - when: { var: { made: 1, held: "main" } }
        do:
          - set: { held: "try", folder: "the snapshot under try-layout" }
          - label: { main: "main", try: "try-layout (thumb)" }
          - mark: { main: "plain", try: "picked" }
          - say: "Your thumb moves to try-layout. The working folder changes to match the snapshot under that label."
    refuse: "Either try-layout is not tied yet, or your thumb is already on it."
  - id: switch-main
    label: "Switch to main"
    cases:
      - when: { var: { held: "try" } }
        do:
          - set: { held: "main", folder: "the snapshot under main" }
          - label: { main: "main (thumb)", try: "try-layout" }
          - mark: { main: "picked", try: "plain" }
          - say: "Your thumb moves back to main. The working folder changes back to the snapshot under main."
    refuse: "Your thumb is already on main."
  - id: snapshot
    label: "Take a snapshot"
    cases:
      - when: { var: { held: "main", beads: 3 } }
        do:
          - spawn: { id: "b4", label: "Snapshot 4", at: "beads.3" }
          - move: { main: "mainl.3" }
          - add: { beads: 1 }
          - say: "Snapshot 4 is taken after the snapshot under main. Only main slides forward to it. The other label stays where it was."
      - when: { var: { held: "main", beads: 4 } }
        do:
          - spawn: { id: "b5", label: "Snapshot 5", at: "beads.4" }
          - move: { main: "mainl.4" }
          - add: { beads: 1 }
          - say: "Snapshot 5 is taken after the snapshot under main. Only main slides forward to it. The other label stays where it was."
      - when: { var: { held: "main", beads: 5 } }
        do:
          - spawn: { id: "b6", label: "Snapshot 6", at: "beads.5" }
          - move: { main: "mainl.5" }
          - add: { beads: 1 }
          - say: "Snapshot 6 is taken after the snapshot under main. Only main slides forward to it. The other label stays where it was."
      - when: { var: { held: "try", beads: 3 } }
        do:
          - spawn: { id: "b4", label: "Snapshot 4", at: "beads.3" }
          - move: { try: "tryl.3" }
          - add: { beads: 1 }
          - say: "Snapshot 4 is taken after the snapshot under try-layout. Only try-layout slides forward to it. The other label stays where it was."
      - when: { var: { held: "try", beads: 4 } }
        do:
          - spawn: { id: "b5", label: "Snapshot 5", at: "beads.4" }
          - move: { try: "tryl.4" }
          - add: { beads: 1 }
          - say: "Snapshot 5 is taken after the snapshot under try-layout. Only try-layout slides forward to it. The other label stays where it was."
      - when: { var: { held: "try", beads: 5 } }
        do:
          - spawn: { id: "b6", label: "Snapshot 6", at: "beads.5" }
          - move: { try: "tryl.5" }
          - add: { beads: 1 }
          - say: "Snapshot 6 is taken after the snapshot under try-layout. Only try-layout slides forward to it. The other label stays where it was."
    refuse: "The string is full in this picture. Press Reset to start again."
script: ["new-label", "switch-try", "snapshot", "snapshot", "switch-main", "snapshot"]
try:
  - text: "Press \"Tie a new label try-layout on this snapshot\" and then \"Switch to try-layout\". Take two snapshots and watch which label moves."
    done: ["new-label", "switch-try", "snapshot", "snapshot"]
  - text: "Press \"Switch to main\". Look at the working folder and at where each label sits."
    done: ["switch-main"]
  - text: "(Optional reveal) With your thumb on main by mistake, take a snapshot. See which label moves."
    done: ["snapshot"]
    optional: true
```

**What you should notice:** Only the label you hold moves forward when you take a snapshot, and the other label stays where it was.

## Build the logic

### Notice

Your album has three photos in a row: 1, 2 and 3. The note `main` is stuck on photo 3. The code in photo 3 works.

You want to try a new idea. It could take a few snapshots, and you may throw it all away.

### Guess

How should you try the idea?

- A. Stick a second note on photo 3 and work from that one. New snapshots then move only that note.
- B. Copy the whole project folder, and work in the copy.
- C. Work on `main` and fix it if it breaks.

Idea B makes copies you must keep in step. Idea C puts a half-finished idea under the note that marks working code. Idea A is the cheap one. A new note costs almost nothing.

### Build

1. Stick a new note on the photo you are on. Nothing else changes yet. Your thumb is still on `main`.
2. Move your thumb to the new note. Your room changes to match the photo under it.
3. Work and take snapshots. Each new photo goes after the last one, and only your thumb's note moves to it.
4. When you want your working code back, move your thumb to the `main` note. Your room goes back to photo 3.
5. If the idea is good, bring its photos into the main line. If main has not moved since you started, the main note can slide forward. If the idea is bad, peel off its note.

Your working code was never touched, because the `main` note stayed on photo 3.

### Name it

- A **branch** is a lightweight movable pointer (a label that points at one commit) to one of the commits. This is how the Git book defines it. Your main line is a branch too. The Git book says it is not a special branch.
- **HEAD** is the pointer to the local branch you are currently on.
- **Switching** means moving HEAD to another branch. The Git book says that when you switch branches, Git resets your working directory to look like it did the last time you committed on that branch.
- **Merging** brings the work of one branch into another. A **fast-forward** is the simple case, when you merge a commit that can be reached by following the first commit's history. Git moves the pointer forward, because there is no divergent work.

| Photo album | Git word | Command |
|---|---|---|
| Stick a new note on this photo | Create a branch | `git branch try-layout` |
| Move your thumb to a note | Switch | `git switch try-layout` |
| Stick a note and move to it | Create and switch | `git switch -c try-layout` |
| Join a note's photos into this line | Merge | `git merge try-layout` |
| Peel a note off | Delete a branch | `git branch -d try-layout` |

The Git book says that `git branch` creates a new pointer to the same commit, and that it does not switch to the new branch. The Git book says Git will not let you switch if uncommitted changes in your working directory conflict with the branch you are switching to.

The name of the main line depends on how the repository was created. The Git documentation says the default name is currently `master`, and that it will change to `main` in Git 3.0. It can also be set with the `init.defaultBranch` setting. So check which name your repository uses.

There is no code to run on this page.

## Predict the output

```challenge
type: predict
run: false
id: where-does-main-point
verified: not-run
code: |
  The album has photos 1, 2 and 3. The note main is on photo 3.
  You run these in order:
    1. git switch -c try-layout
    2. git commit   (makes photo 4)
    3. git commit   (makes photo 5)
  Which photo is the note main on now?
options:
  - id: a
    text: "Photo 5"
    why: "You may have thought every note follows the newest photo. Only the note under your thumb moves when you take a photo."
  - id: b
    text: "Photo 3"
    correct: true
    why: "Your thumb was on try-layout, so try-layout moved to photo 4 and then photo 5. The main note stayed where it was, and your working code is untouched."
  - id: c
    text: "Photo 4"
    why: "You may have thought main moved once and then stopped. It did not move at all, because it was never under your thumb."
output: |
  Photo 3
```

```challenge
type: predict
run: false
id: switch-back-to-main
verified: not-run
code: |
  The album has photos 1 to 5.
  The note main is on photo 3. The note try-layout is on photo 5.
  Your thumb is on try-layout, and your room matches photo 5.
  You run: git switch main

  What is true now?
options:
  - id: a
    text: "The room matches photo 3, and try-layout has been deleted"
    why: "You may have thought leaving a note throws it away. Switching only moves your thumb, and the note stays on its photo."
  - id: b
    text: "The room still matches photo 5, because the branch has newer work"
    why: "You may have thought the room stays put. The Git book says switching resets the working directory to look like the last commit on that branch."
  - id: c
    text: "The room matches photo 3, and try-layout is still on photo 5"
    correct: true
    why: "Moving your thumb changes the room to match photo 3. The other note keeps its place, so the idea is safe."
output: |
  The room matches photo 3, and try-layout is still on photo 5
```

```challenge
type: predict
run: false
id: forgot-to-make-a-branch
verified: not-run
code: |
  The album has photos 1, 2 and 3. The note main is on photo 3.
  Your thumb is on main. You forget to make a new note.
  You try your idea and run: git commit   (makes photo 4)

  Which photo is the note main on now?
options:
  - id: a
    text: "Photo 4, so the unfinished idea is now in the main line"
    correct: true
    why: "The note under your thumb moves forward with each snapshot. Your thumb was on main, so main moved and now holds the half-finished idea."
  - id: b
    text: "Photo 3, because only a new note ever moves"
    why: "You may have thought main is protected. Any branch moves forward when you commit while on it."
  - id: c
    text: "Nowhere, because Git refuses to commit on main"
    why: "You may have expected a warning. Git commits on whatever branch HEAD is on, and main is a branch like any other."
output: |
  Photo 4, so the unfinished idea is now in the main line
```

## Tricks to remember

- A branch is a sticky note, not a copy of the album.
- The note under your thumb is the only one that moves.
- Before you try an idea, make a note and move your thumb to it. Switch your thumb, and the room changes with it.
- Task: make a branch, take one snapshot on it, switch back to your main line, and merge it. Look at the result with `git log`.

## Common mistakes

- **Thinking a branch is a full copy of the project folder.** Switching changes the files in your folder, so it feels like a copy. The Git book says a branch is a lightweight movable pointer to a commit. Think of a label.
- **Thinking `git branch` also switches to the new branch.** The name sounds like an action on you. The Git book says it only creates the pointer. Use `git switch -c` to create and switch in one step.
- **Committing on the main line when you meant a branch.** Your thumb was still on `main`. Before you commit, check which branch you are on. If you already did, the work is now under the working-code note.
- **Being surprised that the files change when you switch.** Commit or put aside your work first, since Git will not switch if uncommitted changes conflict.

## Where this shows up in Android

You ask an AI tool to rewrite a screen. The result might break the app. If you first made a branch for it, your main line still builds, and you can throw the branch away.

My own reasoning: teams use one branch per feature or fix, so that unfinished work stays out of the main line until it is ready.
## Say it in an interview

**What is a Git branch, and how does it let you work without breaking your working code?**
The Git book says a branch is a lightweight movable pointer to one of the commits. Making a new branch costs almost nothing, so I try an idea on its own branch. Commits move only the branch I am on, so my main line stays as it was.

**How do you create, switch and merge branches?**
I run `git switch -c name` to create a branch and switch to it, `git switch name` to move between branches, and `git merge name` while on the target branch. The Git book says `git switch` is available from Git 2.23, and the older way is `git checkout`. If the target has not moved, the merge is a fast-forward, and Git moves the pointer forward.

**What is the difference between merging and rebasing?**
The Git book says rebasing replays changes from one line of work onto another in the order they were introduced, and merging takes the two endpoints and merges them together. The final snapshot is the same, and only the history is different. My own reasoning: a merge keeps the real shape of the history, and a rebase gives a straighter line.

**What goes wrong when you rebase a branch that other people use?**
The Git book gives a rule: do not rebase commits that exist outside your repository and that people may have based work on. Rebasing abandons the old commits and creates new ones, so your teammates' copies no longer match, and they have to re-merge their work. It gets messy when you pull their work back into yours.

## Recap

1. A branch is a movable label on one snapshot, and it is not a copy of the project.
2. Only the branch you are on moves forward when you commit.
3. Switching branches changes the files in your working folder to match that branch.

## You can now

- Predict where each branch points after a short story of commits and switches.
- Explain why a branch leaves your working code untouched, using the sticky notes.
- Name the command that creates a branch and switches to it in one step.
