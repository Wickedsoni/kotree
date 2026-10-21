---
id: tools/git/merge-conflicts
title: "What do I do when git says two changes collide?"
status: live
difficulty: hard
order: 3
requires: [tools/git/branches]
summary: A merge conflict means two copies changed the same part of a file differently, so Git stops and asks you to choose what stays.
video: ""
publishedOn: 2026-10-21
updatedOn: 2026-10-21
sims: [shared-page-corrections]
playground: false
fruit: false
upnext: false
---

## The question

You and a teammate each fix the same sentence in a shared file, in your own ways. You try to join your work, and Git stops. It prints a message about a "conflict" and refuses to go on. Did you break something? What is Git asking you to do?

## Mental model

Think of two people correcting the same sentence on a shared printed page.

- The page is the file you both work on.
- Maya and Ravi each have their own photocopy of the page. Each corrects it with a pen.
- Joining the copies means putting the corrections on one clean page.
- If they corrected different lines, anyone can put both on the page.
- If they corrected the same sentence in different ways, the page cannot know whose correction wins.
- You are the editor at the front desk. You read both and decide.

When the corrections collide, the page does not pick. It prints both versions, one above the other, with divider lines, and waits for the editor.

**Where the analogy stops:** An editor with paper sees at a glance what each person meant. Git compares lines of text and does not understand meaning, so it can join two corrections cleanly and still produce a page that makes no sense (general knowledge). Also, a printed page does not refuse to be saved. Git lets you save the page with the divider lines still on it, as the sim shows.

## See it move

```sim
id: shared-page-corrections
template: zones
title: "Two copies of a page and one joined page"
intro: "Maya and Ravi each corrected their own copy of a shared page. The joined page is empty. By default they fixed different lines."
zones:
  - { id: maya, label: "Maya's copy" }
  - { id: ravi, label: "Ravi's copy" }
  - { id: merged, label: "The joined page" }
items:
  - { id: m, label: "Maya's fix", at: maya }
  - { id: r, label: "Ravi's fix", at: ravi }
vars: { joined: "no", conflict: "no", same: "off", git: "Nothing joined yet" }
show:
  - { var: git, label: "Git says" }
actions:
  - id: join
    label: "Join the two copies"
    cases:
      - when: { var: { joined: "no", same: "off" } }
        do:
          - move: { m: merged, r: merged }
          - set: { joined: "yes", git: "Joined on its own" }
          - say: "Maya and Ravi fixed different lines, so Git puts both fixes on the page without asking you."
      - when: { var: { joined: "no", same: "on" } }
        do:
          - move: { m: merged, r: merged }
          - spawn: { id: top, label: "Marker line: Maya's side starts", at: merged }
          - spawn: { id: mid, label: "Marker line: the divider", at: merged }
          - spawn: { id: bottom, label: "Marker line: Ravi's side ends", at: merged }
          - set: { joined: "yes", conflict: "yes", git: "CONFLICT, waiting for you" }
          - say: "Both people changed the same sentence. Git stops and prints both versions on the page between marker lines. It does not choose for you."
    refuse: "The copies are already joined. Press Reset to start again."
  - id: keep-mine
    label: "Keep Maya's side"
    cases:
      - when: { var: { conflict: "yes" } }
        do:
          - remove: top
          - remove: mid
          - remove: bottom
          - remove: r
          - set: { conflict: "no", git: "Conflict settled, page not saved yet" }
          - say: "The marker lines and Ravi's version are gone. Maya's version stays on the page."
    refuse: "There is no conflict to settle."
  - id: keep-theirs
    label: "Keep Ravi's side"
    cases:
      - when: { var: { conflict: "yes" } }
        do:
          - remove: top
          - remove: mid
          - remove: bottom
          - remove: m
          - set: { conflict: "no", git: "Conflict settled, page not saved yet" }
          - say: "The marker lines and Maya's version are gone. Ravi's version stays on the page."
    refuse: "There is no conflict to settle."
  - id: keep-both
    label: "Keep both sides"
    cases:
      - when: { var: { conflict: "yes" } }
        do:
          - remove: top
          - remove: mid
          - remove: bottom
          - set: { conflict: "no", git: "Conflict settled, page not saved yet" }
          - say: "The marker lines are gone and both versions stay, one under the other. Read the page to check it still makes sense."
    refuse: "There is no conflict to settle."
  - id: mark-resolved
    label: "Save the page and mark it resolved"
    cases:
      - when: { var: { conflict: "yes" } }
        do:
          - set: { git: "Marked resolved, but marker lines are still on the page" }
          - say: "Staging the page marks it resolved, but the marker lines are still on it. The saved page is broken."
      - when: { var: { joined: "yes", conflict: "no" } }
        do:
          - set: { git: "Resolved and saved" }
          - say: "The page is staged and marked resolved. It has no marker lines, so it is ready for the commit."
    refuse: "There is nothing to save yet. Join the two copies first."
toggles:
  - { id: same, label: "Both people fix the same sentence", var: same }
script: [join, mark-resolved]
try:
  - text: 'Press "Join the two copies". The two fixes are on different lines.'
    done: [join]
  - text: 'Switch on "Both people fix the same sentence". The same join now stops with a conflict.'
    done: ["same=on"]
  - text: 'Without choosing, press "Save the page and mark it resolved" and read what Git says.'
    done: [mark-resolved]
  - text: 'Press "Keep both sides", then press "Save the page and mark it resolved" again.'
    done: [keep-both, mark-resolved]
    optional: true
```

**What you should notice:** Fixes on different lines are joined without a question, a fix to the same sentence stops for your decision, and the page is broken if the marker lines stay on it.

## Build the logic

### Notice

Maya and Ravi both start from the same page. Maya changes the opening-hours sentence to "We open at 9 am." Ravi changes the same sentence to "We open at nine." Each saves a clean copy. Now someone must join the two copies into one page.

### Guess

What should happen when both changed the same sentence?

- A. Use the correction that was made last.
- B. Stop, show both versions to a person, and let the person choose.
- C. Mix words from both versions to make a new sentence.

Idea A throws away one person's work without telling anyone. Idea C invents a sentence that neither person wrote. Idea B is the editor at the front desk.

### Build

1. Take the page as it was before either person changed it.
2. Look at what each person changed.
3. If they changed different parts, put both changes on the page. Nobody needs to decide.
4. If they changed the same part in different ways, stop. Do not guess.
5. Print both versions on the page, one above the other, with divider lines that say which side is which.
6. The editor reads both and chooses: keep one side, keep the other, or write a new version.
7. Delete the divider lines. Tell Git the page is finished, and then save the joined page.

### Name it

To **merge** is to join the work of two branches into one. A branch is a movable label on a snapshot, as in the last lesson. A **merge conflict** happens, according to the Git book, when you changed the same part of the same file differently in the two branches you are merging. Git then pauses and does not create a merge commit. When the changes touch different parts, Git can join them itself.

Git writes **conflict markers** into the file. The Git book gives this layout. The top part, above `=======`, is the version in **HEAD**, which is the branch you had checked out when you ran the merge. The bottom part is the version from the other branch.

```kotlin
// does not compile
fun openingHours(): String {
<<<<<<< HEAD
    return "We open at 9 am."
=======
    return "We open at nine."
>>>>>>> ravi-fix
}
```

This file does not compile because the three marker lines are not Kotlin. To resolve the conflict, the Git book says you choose one side or the other, or merge the contents yourself, and the marker lines must be removed completely. Then you run `git add` on the file to mark it as resolved. The book says staging the file marks it as resolved. **Staging** means choosing what goes into the next snapshot, as in the first lesson of this stem.

| Photocopied page | Git |
|---|---|
| Join the copies | `git merge ravi-fix` |
| See which pages are waiting for the editor | `git status`, which lists "Unmerged paths" |
| The editor chooses and removes the divider lines | Edit the file |
| Tell Git the page is finished | `git add <file>` |
| Save the joined page | `git commit` |

My own reasoning: Git does not check whether you removed the marker lines when you stage the file, as the sim shows. Read the file once more before you stage it.

You do not have to edit by hand. `git mergetool` starts a visual merge tool, according to the Git book. Android Studio is built on IntelliJ IDEA (general knowledge), and the IntelliJ documentation describes a Conflicts dialog. For each file it offers **Accept Yours**, which keeps the changes from the currently checked-out branch, **Accept Theirs**, which accepts the changes from the branch you are merging, and a choice to resolve each conflict manually. Resolving manually opens a **merge tool** with a left pane, a right pane and a central pane that you can edit. The central pane holds the result.

There is no code to run on this page. It follows a small page on paper.

## Predict the output

```challenge
type: predict
run: false
id: which-side-is-head
verified: not-run
code: |
  You are on your main branch and you run: git merge ravi-fix
  The file now contains:

    fun openingHours(): String {
    <<<<<<< HEAD
        return "We open at 9 am."
    =======
        return "We open at nine."
    >>>>>>> ravi-fix
    }

  Which version is the top part, above =======?
options:
  - id: a
    text: "The version from ravi-fix, the branch you merged in"
    why: "You may have thought the top part is the incoming work. The marker line after the top part names the branch, and the top one says HEAD, which is where you were working."
  - id: b
    text: "The version in HEAD, the branch you had checked out when you ran the merge"
    correct: true
    why: "The Git book says the version in HEAD is the top part of the block, and the other branch is the bottom part. Maya's copy sits on top."
  - id: c
    text: "The older version, with the newer one below it"
    why: "You may have thought the markers sort by time. They sort by side: your side on top, the other branch below."
output: |
  The version in HEAD, the branch you had checked out when you ran the merge
```

```challenge
type: predict
run: false
id: keep-both-sides
verified: not-run
code: |
  The file contains this conflict:

    fun openingHours(): String {
    <<<<<<< HEAD
        return "We open at 9 am."
    =======
        return "We open at nine."
    >>>>>>> ravi-fix
    }

  You choose to keep both sides, top first, and you delete
  the three marker lines. Which lines are inside the function?
options:
  - id: a
    text: "Only: return \"We open at nine.\""
    why: "You may have thought keeping both means keeping the newer one. That would be keeping only Ravi's side. Keeping both leaves both return lines on the page."
  - id: b
    text: "return \"We open at nine.\" and then return \"We open at 9 am.\""
    why: "You may have swapped the order. The top part stays above the bottom part when you only delete the marker lines."
  - id: c
    text: "return \"We open at 9 am.\" and then return \"We open at nine.\""
    correct: true
    why: "Only the three marker lines are removed, so Maya's line stays on top and Ravi's line stays below it. Git accepts the page. A reader must still check that the page makes sense, and a second return line never runs."
output: |
  return "We open at 9 am." and then return "We open at nine."
```

```challenge
type: predict
run: false
id: different-lines-same-file
verified: not-run
code: |
  Maya changes line 2 of Notes.kt in her copy.
  Ravi changes line 40 of Notes.kt in his copy.
  Someone merges the two copies. What does Git do?
options:
  - id: a
    text: "It joins both changes on its own, because they touch different parts of the file"
    correct: true
    why: "The Git book says a conflict comes from changing the same part of the same file differently. These changes are far apart, so the page can take both without asking the editor."
  - id: b
    text: "It stops with a conflict, because both people changed the same file"
    why: "You may have thought one file means one conflict. The Git book ties a conflict to the same part of the file, not to the file as a whole."
  - id: c
    text: "It keeps the changes from the branch you are on and drops the other"
    why: "You may have thought Git picks a winner to save work. Git never drops a change without asking, so the other fix would be lost."
output: |
  It joins both changes on its own, because they touch different parts of the file
```

## Tricks to remember

- Same sentence, two corrections: the page asks the editor.
- Top is HEAD, the bottom is the other branch.
- Delete all three marker lines, then stage the file.
- A conflict is Git asking a question, not an error you caused.
- Task: make a branch, change one line in the branch and the same line on main, merge, find the markers, keep both sides, and fix the result.

## Common mistakes

- **Thinking a conflict means the code is broken or I did something wrong.** The word sounds like an error. It means two people changed the same part, and Git needs a human to choose. Read both versions and decide.
- **Choosing Accept Yours or Accept Theirs without reading.** It clears the red marks fast. One side may hold a bug fix that disappears. Read both sides and keep what the code needs.
- **Staging a file that still has marker lines.** The conflict is gone from the status list, so it feels finished. The markers are now part of the saved file, and the build fails. Search for `<<<<<<<` before you stage.
- **Thinking a clean join means correct code.** Git joined the lines without asking, so it feels safe. Git joins by lines and not by meaning (general knowledge). Build and run your tests after every merge.

## Where this shows up in Android

You pull the latest work, and Android Studio shows a Conflicts dialog for `strings.xml`. You and a teammate both changed the same line (an example). Each side has its own wording. You open the merge tool, choose, save, and the dialog closes.

A second symptom is a build that fails with errors on lines that start with `<<<<<<<` or `=======`. Someone saved a file with the marker lines still in it. The Build step 7 explains the repair: delete the divider lines and read the page again.

## Say it in an interview

**What is a Git merge conflict, and when does it occur?**
The Git book says it happens when you changed the same part of the same file differently in the two branches you are merging. Git cannot join them cleanly, so it pauses, writes markers into the file and waits for me. If the changes touch different parts, Git joins them itself.

**How do you read and resolve conflict markers?**
The top part above `=======` is the version in HEAD, which is where I was when I ran the merge. The bottom part is the other branch. I choose one side or write a version that uses both, delete all the marker lines, run `git add` to mark the file resolved, and then commit.

**How does Android Studio help with conflicts?**
Android Studio is built on IntelliJ IDEA (general knowledge). The IntelliJ documentation describes a Conflicts dialog with Accept Yours, Accept Theirs and a manual merge. The manual merge tool has a left pane, a right pane and an editable central pane that holds the result.

**What goes wrong when someone accepts "ours" or "theirs" without checking?**
This answer is my own reasoning. One side may hold a bug fix or a business rule that is silently thrown away, and the code still builds. Two changes can also join cleanly and still be wrong together. So I read both sides, build the project and run the tests before I commit the merge.

## Recap

1. A merge conflict means two copies changed the same part of a file differently.
2. Git writes both versions between marker lines, and you choose what stays.
3. Delete every marker line before you stage the file, then build and test.

## You can now

- Predict which side of a conflict block is HEAD and which is the other branch.
- Predict what stays in a file after you keep both sides and delete the markers.
- Explain why two changes on different lines join without a conflict.
