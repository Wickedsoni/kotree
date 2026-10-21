---
id: tools/git/what-is-a-commit
title: How do I save a snapshot of my project that I can return to later?
status: shadow
difficulty: medium
order: 1
requires: []
summary: A commit is a saved snapshot of your project. You choose what goes in it first, and Git keeps every old snapshot so you can go back.
video: ""
publishedOn:
updatedOn:
sims: [snapshot-tray]
playground: false
fruit: false
upnext: false
---

<!-- SOURCES (read 21 Oct 2026, from the Pro Git book source at github.com/progit/progit2, because git-scm.com could not be opened from here): book/02-git-basics/sections/recording-changes.asc ("Git stages a file exactly as it is when you run the git add command"; "the commit records the snapshot you set up in your staging area"; "Anything you didn't stage is still sitting there modified"; the git commit -m example; a file named .gitignore lists patterns to leave out; git restore --staged to unstage) and book/01-introduction/sections/what-is-git.asc (Git thinks of its data as snapshots; modified, staged and committed; "Committed means that the data is safely stored in your local database"; most operations need only local files). git restore: a search excerpt of the git-restore manual says the default source for the working tree is the index and --source picks a different commit; the manual page itself was not opened. The default branch name is not stated on purpose. No git command was run for this lesson. General knowledge or my own reasoning, labelled in the text: why a password in an old snapshot is hard to take back, build output files, and what a good commit message looks like. Interview data: docs/practice/interview-tools.md. Sim snapshot-tray is new, see docs/sims/needed-ids.md. Predict items are traced by hand. -->

## The question

You spend an evening changing your project. The next morning nothing works, and you wish you could go back to last night. Is there a way to save your project as it is right now, so you can always return to it? And what exactly gets saved?

## Mental model

Think of a photo album of your room.

- Your room is your project folder, the place where you work.
- The frame is the small area where you choose what will be in the next photo.
- The photo is a snapshot of the things you put in the frame.
- The caption is the short note you write under the photo.
- The album keeps every old photo, in order.

You tidy the desk, you move the things you want to keep into the frame, and you take the photo. Then you write a caption. Later, you can open the album and look at any old photo.

**Where the analogy stops:** A photo shows the whole room, including what you did not mean to show. A snapshot holds only what you placed in the frame, and the rest of the room stays as it is. Also, an old photo can only be looked at, but an old snapshot can bring a file back into your folder.

## See it move

<!-- SIM: snapshot-tray -->

**Try this:**
1. Edit file A. It moves to the "changed" area of the working folder.
2. Drag file A into the tray, which stands for the frame. Then press "Take snapshot".
3. Edit file B and leave it out of the tray. Take another snapshot and open it.

**What you should notice:** A snapshot holds only the files that were in the tray, and the edit you left out is still waiting in your folder.

## Build the logic

### Notice

You work on two files today. You fix a spelling mistake in the notes file. You also start a change in the to-do file, but you are not finished with it.

You want to save the notes file and not the half-finished to-do file. The Save button in your editor cannot do that. It saves the file you are in, and it does not say which changes belong together.

### Guess

How should the saving work, so you can pick what goes in?

- A. Choose the files first, then take the snapshot of only those.
- B. Save every file, every time, whether it is ready or not.
- C. Let the computer guess which files are ready.

Idea B saves the half-finished work and mixes it with the finished work. Idea C means the computer decides for you and is sometimes wrong. Idea A is the frame and the photo.

### Build

1. Work in your folder as usual. Files change.
2. Choose which changed files belong in the next snapshot, and put them in the frame.
3. Take the snapshot. Only the framed files go in.
4. Write a caption that says what the snapshot is about.
5. Keep working. The files you left out are still changed, waiting for a later snapshot.

If you forget step 2 for a file, that file is not in the snapshot, and nothing stops you.

### Name it

- **Git** is the tool that keeps all your snapshots in order.
- A **commit** is a saved snapshot of your project, with a caption. The Git book says a committed snapshot is safely stored in your local database, which is on your own computer.
- **Staging** means choosing which changed files go into the next commit. The place where you put them is the **staging area**.
- A **commit message** is the caption: one short line that says what changed.

| Photo album | Git word | Command |
|---|---|---|
| Put things in the frame | Stage | `git add <file>` |
| Take the photo | Commit | `git commit` |
| Take the photo with its caption | Commit with a message | `git commit -m "..."` |

Two facts from the Git book matter here. First, Git stages a file exactly as it is when you run `git add`. If you change the file again afterwards, the new change is not in the frame until you add it again. Second, the commit records the snapshot you set up in the staging area, and anything you did not stage is still sitting there modified.

To bring a file back from an old snapshot, you use `git restore` and name that snapshot with `--source`. Without `--source`, the file comes back from the staging area, not from an old photo.

Git also reads a file called `.gitignore`. You list patterns in it for files that Git should leave out. Because the album keeps every old photo, never commit a password or a key. My own reasoning: a password that is in an old snapshot is still in the album after you delete it from the folder.

There is no code to run on this page. It follows a small story on paper.

## Predict the output

```challenge
type: predict
run: false
id: which-edits-are-in
verified: not-run
code: |
  You do these five things in order:
    1. Edit file A.
    2. Stage file A (put it in the frame).
    3. Edit file B.
    4. Commit (take the snapshot).
    5. Edit file A again.
  Which edits are in the snapshot?
options:
  - id: a
    text: "The edit of A and the edit of B"
    why: "You may have thought a commit takes every changed file. It only takes what you staged. B was never put in the frame."
  - id: b
    text: "Only the first edit of A"
    correct: true
    why: "Only what was in the frame when the photo was taken goes in. That is A from step 1. B was left out, and the later edit of A happened after the photo."
  - id: c
    text: "Both edits of A and the edit of B"
    why: "You may have thought the commit takes the latest version of everything. The photo is taken at the moment of the commit. The second edit of A came after it."
output: |
  Only the first edit of A
```

```challenge
type: predict
run: false
id: ten-saves-one-commit
verified: not-run
code: |
  You press Save in your editor ten times while you work.
  Then you stage the file and make one commit.
  How many snapshots does the album gain?
options:
  - id: a
    text: "Ten, one for every time you pressed Save"
    why: "You may have thought Git watches every save. Saving only writes to your folder. The album gets a photo only when you take one."
  - id: b
    text: "None, because a commit works only after it is uploaded to the internet"
    why: "You may have thought a commit means upload. The Git book says a committed snapshot is stored in your local database. Sending it somewhere is a separate step."
  - id: c
    text: "One"
    correct: true
    why: "Saving changes the room. A photo is taken only when you commit. Ten saves and one commit give one new snapshot."
output: |
  One
```

## Tricks to remember

- Frame first, photo second, caption last.
- Save changes your folder. Commit changes your album.
- A commit holds what you staged, not what you changed.
- Never put a secret in the frame. The album keeps every old photo.

## Common mistakes

- **Thinking Git saves the file every time you press Save.** It feels that way because both are called saving. Pressing Save writes to your folder. Git records a snapshot only when you commit.
- **Thinking a commit uploads your project to the internet.** Sites that host code make it feel like one step. A commit is stored on your own computer, and sending it to a site is a separate step.
- **Committing and finding that the last change is missing.** You edited a file and committed without staging it again, so the new change was not in the frame. Check what is staged before you commit, and stage again after a new change.

## Where this shows up in Android

You change an Android project, and the app stops building. If you committed an hour ago, you can go back to that snapshot and compare it with your folder to see what changed. That is the "I wish I could go back" moment from the start of this page.

A real Android project also has files that the build tools create (general knowledge). A `.gitignore` file tells Git to leave those out of your commits. The same file can keep a file that holds a password or key out of your commits, as long as you list it before you commit.

## Say it in an interview

**What is a Git commit?**
A commit is a saved snapshot of your project at one moment, with a message. The Git book says Git thinks of its data as snapshots, and a committed snapshot is stored in your local database. Every commit stays in the history, so you can go back to it.

**How do you stage changes and record a commit?**
I run `git add` on the files I want, then `git commit -m` with a short message. The Git book says Git stages a file as it is when I run `git add`, so a later edit needs another `git add`. Anything I did not stage is not in the commit.

**Is a commit a list of changes, or a snapshot?**
The Git book describes it as a snapshot of the project's files at that moment, not a list of changes. The trade-off is that you think in whole states you can return to. It also helps to remember that a commit only holds what was staged.

**Why keep each commit small and about one thing?**
This answer is my own reasoning and general knowledge. A commit about one thing is quick to read, review and go back to. If one commit mixes unrelated changes, undoing one of them also undoes the others, and the message cannot describe it well.

## Recap

1. A commit is a saved snapshot with a caption, stored on your own computer.
2. You stage the files you want first, and only staged files go into the commit.
3. The album keeps every snapshot, so you can go back to an old one.

## You can now

- Predict which edits end up in a snapshot after a short story of edits.
- Predict how many snapshots the album gains after several saves and one commit.
- Explain staging and committing using the photo album.
