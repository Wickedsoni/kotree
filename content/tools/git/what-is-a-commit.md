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

<!-- ⚠️ VERIFY: (1) sim id snapshot-tray has no spec in docs/sims yet (physics-designer: a working folder, a small tray and a shelf of snapshots; learner moves a changed file into the tray, takes a snapshot, and sees an edit left outside the tray is not in it). (2) Facts checked 21 Oct 2026: git-scm.com/book/en/v2/Git-Basics-Recording-Changes-to-the-Repository ("add precisely this content to the next commit", "Git stages a file exactly as it is when you run the git add command", "the commit records the snapshot you set up in your staging area. Anything you didn't stage is still sitting there modified", .gitignore lists patterns of files to ignore); git-scm.com/book/en/v2/Getting-Started-What-is-Git%3F (Git thinks of data as "a series of snapshots", the three states modified, staged and committed, "committed means that the data is safely stored in your local database", most operations need only local files); git-scm.com/docs/git-restore (restores working-tree files from a source; by default from the index, and --source=<tree> takes a commit). (3) The default branch name is deliberately NOT stated: the book still says Git's default is master while GitHub uses main, so the lesson avoids the word. Re-read before live if a branch name is added. (4) The command line is not run: no git run on this machine for this lesson; the lesson shows command names only in plain text. (5) The roadmap F item (a commit made with a new file never added) was not written: playground false; the roadmap B item (delete a file and bring it back) became a sentence, not a task. (6) The roadmap row says to link to the secrets lesson; there is no such leaf yet, so the lesson states the rule in words. (7) Roadmap table title differs from the YAML title; the YAML title is used here. Assumes foundation/first-code/run-your-first-program (not drafted yet). Predict items traced by hand only. -->
## The question

You spend an evening changing your project. The next morning nothing works. You wish you could go back to last night, before you started.

Is there a way to save your project as it is right now, so you can always return to it? And what exactly gets saved?

## Mental model

Think of a photo album of your room.

- Your room is your project folder, the place where you work.
- The frame is the small area where you choose what will be in the next photo.
- The photo is a snapshot of the things you put in the frame.
- The caption is the short note you write under the photo.
- The album keeps every old photo, in order.

You tidy the desk, you move the things you want to keep into the frame, and you take the photo. Then you write a caption. Later, you can open the album and look at any old photo.

**Where the analogy stops:** A photo shows the whole room, including what you did not mean to show. A snapshot holds only what you placed in the frame, and the rest of the room stays as it is. Also, you can open an old photo to look, but an old snapshot can bring a file back into your folder. A photo cannot bring a vase back.

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

You want to save the notes file and not the half-finished to-do file. A normal "save" cannot do that. It saves the file you are in and does not tell anyone what belongs together.

### Guess

How should the saving work, so you can pick what goes in?

- A. Save every file, every time, whether it is ready or not.
- B. Let the computer guess which files are ready.
- C. Choose the files first, then take the snapshot of only those.

Idea A saves the half-finished work and mixes it with the finished work. Idea B means the computer would decide for you and sometimes be wrong. Idea C is the frame and the photo.

### Build

1. Work in your folder as usual. Files change.
2. Choose which changed files belong in the next snapshot, and put them in the frame.
3. Take the snapshot. Only the framed files go in. The frame is now empty.
4. Write a caption that says what the snapshot is about.
5. Keep working. The files you left out are still changed, waiting for a later snapshot.

If you forget step 2 for a file, that file is not in the snapshot, and nothing warns you.

### Name it

- A **commit** is a saved snapshot of your project, with a caption. It is stored on your own computer.
- **Staging** means choosing which changed files go into the next commit. The place where you put them is the **staging area**.
- A **commit message** is the caption: one short line that says what changed.
- **Git** is the tool that keeps all the commits in order.

| Photo album | Git word | Command |
|---|---|---|
| Put things in the frame | Stage | `git add <file>` |
| Take the photo | Commit | `git commit` |
| The caption | Commit message | `git commit -m "..."` |
| Bring a file back from an old photo | Restore | `git restore` |

Two facts help here. Git stages a file as it is at the moment you run `git add`. If you change the file again afterwards, the new change is not in the frame until you add it again. And a commit lives on your own computer. It does not go anywhere on the internet unless you send it later.

Git also has a file called `.gitignore`. You list the patterns of files in it that Git should leave out, such as files that your tools create. The album keeps every old photo, so never commit a password or a key.

There is no code to run on this page. This lesson follows a small story on paper.

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
    text: "Only the first edit of A"
    correct: true
    why: "Only what was in the frame when the photo was taken goes in. That is A from step 1. B was left out, and the later edit of A happened after the photo."
  - id: b
    text: "Edit of A and edit of B"
    why: "You may have thought a commit takes every changed file. It only takes what you staged. B was never put in the frame."
  - id: c
    text: "Both edits of A and the edit of B"
    why: "You may have thought the commit takes the latest version of everything. The photo is taken at the moment of the commit. The second edit of A came after it."
output: "Only the first edit of A"
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
    text: "One"
    correct: true
    why: "Saving changes the room. A photo is taken only when you commit. Ten saves and one commit give one new snapshot."
  - id: c
    text: "None, because commits are uploaded to the internet first"
    why: "You may have thought a commit means upload. A commit is kept on your own computer. Sending it somewhere is a separate step."
output: "One"
```

## Tricks to remember

- Frame first, photo second, caption last.
- Save changes your folder. Commit changes your album.
- A commit holds what you staged, not what you changed.
- Never put a secret in the frame. The album keeps every old photo.

## Common mistakes

- **Thinking Git saves the file every time you press Save.** It feels that way because both are "saving". Pressing Save writes to your folder. Git only records a snapshot when you commit.
- **Thinking a commit uploads your project to the internet.** Sites that host code make it feel like one step. A commit stays on your computer. Sending it to a site is a separate step, covered in another lesson.
- **Committing and finding that the last change is missing.** You edited a file, and then you committed without staging it, so it was not in the frame. Check what is staged before you commit. Git stages a file as it is when you run `git add`, so stage again after a new change.

## Where this shows up in Android

You change an Android project, and the app stops building. If you committed an hour ago, you can look at that snapshot and compare it with your folder to see what changed. That is the "I wish I could go back" moment from the start of this lesson.

A real Android project also has files that tools create, such as build output. A `.gitignore` file tells Git to leave those out of the commit. The same file can keep a file that holds a password or a key out of your commits. A commit keeps old versions, so a password is hard to take back once it is in.

## Recap

1. A commit is a saved snapshot with a caption, kept on your own computer.
2. You stage the files you want first, and only staged files go into the commit.
3. The history keeps every snapshot, so you can go back to an old one.

## You can now

- Explain staging and committing using the photo album.
- Predict which edits end up in a snapshot after a short story of edits.
- Say why pressing Save and making a commit are not the same thing.
