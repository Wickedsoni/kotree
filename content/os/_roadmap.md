```yaml
branch: os
bough: cs-theory
stems:
  - id: what-an-os-does
    title: What an Operating System Does
    why: Every app runs on top of an operating system, so a fresher needs to see what it hands out and why an app must ask instead of grabbing.
    leaves:
      - id: what-the-os-does-for-apps
        title: What does the operating system do for my app that my code cannot do alone?
        difficulty: easy
        requires: []
        fruit: false
      - id: kernel-and-system-calls
        title: How does my app ask the operating system to use the hardware for it?
        difficulty: easy
        requires: [os/what-an-os-does/what-the-os-does-for-apps]
        fruit: false
  - id: processes
    title: Processes
    why: A process is the box an app runs in, and it explains why one app can crash while the others carry on.
    leaves:
      - id: what-is-a-process
        title: Why does one crashing app leave all the other apps running?
        difficulty: easy
        requires: [os/what-an-os-does/kernel-and-system-calls]
        fruit: false
      - id: process-states
        title: What is a process doing while it waits for its turn?
        difficulty: medium
        requires: [os/processes/what-is-a-process]
        fruit: false
      - id: passing-data-between-processes
        title: How can two apps share data if neither can see the other's memory?
        difficulty: medium
        requires: [os/processes/what-is-a-process, os/what-an-os-does/kernel-and-system-calls]
        fruit: false
  - id: memory
    title: Memory
    why: Crashes named StackOverflowError and OutOfMemoryError, and leaked screens, all come from how a process uses its memory.
    leaves:
      - id: stack-and-heap
        title: Why do local variables disappear when a function ends but objects can live on?
        difficulty: medium
        requires: [os/processes/what-is-a-process]
        fruit: false
      - id: what-keeps-an-object-alive
        title: Why is an object I no longer need still using memory?
        difficulty: medium
        requires: [os/memory/stack-and-heap]
        fruit: false
      - id: virtual-memory
        title: Why does every app think it has the whole memory to itself?
        difficulty: hard
        requires: [os/processes/what-is-a-process, os/memory/stack-and-heap]
        fruit: false
  - id: threads
    title: Threads
    why: The main thread, background threads and every freeze story rest on what a thread is and what it costs.
    leaves:
      - id: what-is-a-thread
        title: How is a thread different from a process?
        difficulty: medium
        requires: [os/processes/what-is-a-process, os/memory/stack-and-heap]
        fruit: false
      - id: what-a-thread-costs
        title: Why not start a new thread for every small job?
        difficulty: easy
        requires: [os/threads/what-is-a-thread]
        fruit: false
      - id: a-thread-that-waits
        title: What happens to a thread while it waits for a file or a reply?
        difficulty: medium
        requires: [os/threads/what-is-a-thread, os/processes/process-states]
        fruit: false
  - id: scheduling
    title: Scheduling
    why: One CPU core serves many apps by taking turns, which explains stutter and why a background job can wait a long time.
    leaves:
      - id: one-cpu-many-apps
        title: How can one CPU core run ten apps at the same time?
        difficulty: easy
        requires: [os/processes/process-states, os/threads/what-is-a-thread]
        fruit: false
      - id: priorities-and-starvation
        title: Why can a low-priority job wait forever?
        difficulty: easy
        requires: [os/scheduling/one-cpu-many-apps]
        fruit: false
  - id: synchronisation
    title: Synchronisation
    why: Two threads touching one value lose updates in ways that are hard to see, and generated code makes this mistake with a tidy face.
    leaves:
      - id: race-conditions
        title: Why can two threads updating one counter lose an update?
        difficulty: hard
        requires: [os/threads/what-is-a-thread, os/scheduling/one-cpu-many-apps]
        fruit: false
      - id: locks-and-critical-sections
        title: How does a lock let only one thread into a risky piece of code?
        difficulty: medium
        requires: [os/synchronisation/race-conditions]
        fruit: false
      - id: producer-and-consumer
        title: How can one thread hand work to another without wasting CPU?
        difficulty: medium
        requires: [os/synchronisation/locks-and-critical-sections, os/threads/a-thread-that-waits]
        fruit: false
  - id: deadlock
    title: Deadlock
    why: A screen can freeze with no heavy loop in sight when two threads each hold what the other needs.
    leaves:
      - id: what-is-a-deadlock
        title: How can two threads wait for each other forever?
        difficulty: hard
        requires: [os/synchronisation/locks-and-critical-sections]
        fruit: false
  - id: files
    title: Files
    why: Apps save drafts, caches and databases as files, and a fresher needs to know who may open them and why reading them is slow.
    leaves:
      - id: files-folders-and-owners
        title: Why can my app open some folders but not others?
        difficulty: easy
        requires: [os/processes/what-is-a-process, os/what-an-os-does/kernel-and-system-calls]
        fruit: false
      - id: why-disk-is-slow
        title: Why is reading a file so much slower than reading a variable?
        difficulty: easy
        requires: [os/files/files-folders-and-owners, os/threads/a-thread-that-waits]
        fruit: false
  - id: android-os
    title: Android Under the App
    why: Android adds its own way of starting apps, talking to system services and closing apps, and each one explains a behaviour a fresher sees.
    leaves:
      - id: zygote-and-app-processes
        title: How does Android create an app process so quickly?
        difficulty: medium
        requires: [os/processes/what-is-a-process, os/memory/virtual-memory]
        fruit: false
      - id: binder-and-system-services
        title: How does my app call a system service that lives in another process?
        difficulty: medium
        requires: [os/processes/passing-data-between-processes, os/what-an-os-does/kernel-and-system-calls]
        fruit: false
      - id: low-memory-killer
        title: Why did Android close my app while I was away?
        difficulty: medium
        requires: [os/memory/virtual-memory, os/android-os/zygote-and-app-processes]
        fruit: false
  - id: putting-it-together
    title: Putting It Together
    why: The branch ability is to name the operating-system event behind a symptom, so the branch ends on matching symptoms to causes.
    leaves:
      - id: match-symptoms-to-causes
        title: Which operating-system event explains this app symptom?
        difficulty: hard
        requires: [os/deadlock/what-is-a-deadlock, os/files/why-disk-is-slow, os/android-os/low-memory-killer, os/memory/what-keeps-an-object-alive]
        fruit: true
```

## Branch prerequisites

One leaf is live since 21 Oct 2026; the rest are `shadow`. Phases follow `docs/08` §10.1: `what-an-os-does`, `processes`, `memory` and `threads` are phase 3; `scheduling`, `synchronisation` and `deadlock` are phase 4; `files`, `android-os` and `putting-it-together` are phase 6. Android framework code does not run in the browser editor and there is no Android emulator or simulator (L15, D8). Most challenges are small plain-Kotlin models, printed output, or a diagram to read. ⚠️ VERIFY on kotlinlang.org that the embedded Kotlin Playground can start threads before any thread leaf goes live.

Rule for the phase checks below: phase 3 stems here may assume only Foundation, Kotlin and phase-2 DSA, because Android core and the phase-3 DSA stems come after `os` in the tie-break order. Android core, DSA and Concurrency leaves are therefore "Helps to know" for phases 3 and 4. Phase 6 stems may assume Android core and Concurrency.

- Assumes Foundation: `foundation/how-computers-work/what-is-an-os` (phase 1) — the first leaf deepens it; it is not re-taught from zero.
- Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu` (phase 1) — used by kernel-and-system-calls, process-states and one-cpu-many-apps.
- Assumes Foundation: `foundation/how-computers-work/memory-vs-storage` (phase 1) — used by virtual-memory, files-folders-and-owners and why-disk-is-slow.
- Assumes Foundation: `foundation/programming-thinking/what-is-a-function` and `foundation/programming-thinking/trace-code-by-hand` (phase 1) — used by stack-and-heap and every ordering task in race-conditions.
- Assumes Foundation: `foundation/from-code-to-running/what-is-art` (phase 1) — used by what-keeps-an-object-alive and zygote-and-app-processes.
- Assumes Kotlin language: `kotlin/classes-and-objects/objects-are-references` (phase 2) — used by stack-and-heap and what-keeps-an-object-alive.
- Assumes Kotlin language: `kotlin/functions/declaring-functions` (phase 2) — used by stack-and-heap.
- Assumes Kotlin language: `kotlin/lambdas/lambda-syntax` and `kotlin/lambdas/closures` (phase 2) — thread bodies and shared counters are lambdas.
- Assumes Kotlin language: `kotlin/types-and-variables/val-vs-var` (phase 2) — used by race-conditions.
- Assumes Android core and lifecycles: `android-core/what-is-an-app/who-starts-your-app` and `android-core/app-startup/cold-warm-hot-start` (phase 3 and 5) — used by zygote-and-app-processes (phase 6).
- Assumes Android core and lifecycles: `android-core/process-death/what-is-process-death` and `android-core/background-work/why-background-is-limited` (phase 3 and 5) — used by low-memory-killer (phase 6).
- Assumes Android core and lifecycles: `android-core/lifecycle/leaking-an-activity` (phase 3) — used by match-symptoms-to-causes (phase 6).
- Assumes Concurrency: `concurrency/why-the-main-thread/why-blocking-freezes-the-screen` (phase 4) — used by match-symptoms-to-causes (phase 6).
- Helps to know DSA: `dsa/stacks/what-is-a-stack` and `dsa/recursion/the-call-stack` — stack-and-heap recaps the pile of function calls in one paragraph.
- Helps to know DSA: `dsa/queues/what-is-a-queue` — the ready queue and the producer belt.
- Helps to know DSA: `dsa/heaps/priority-queue-idea` — priorities-and-starvation.
- Helps to know DSA: `dsa/trees/what-is-a-tree` — folders form a tree.
- Helps to know Android core and lifecycles: `android-core/what-is-an-app/who-starts-your-app`, `android-core/lifecycle/leaking-an-activity`, `android-core/files-and-sharing/app-private-files` and `android-core/intents/what-is-an-intent` — named as the app-side view of a few leaves; each is recapped in one paragraph.
- Helps to know Concurrency: `concurrency/why-the-main-thread/what-the-main-thread-does`, `concurrency/threads/why-raw-threads-get-messy`, `concurrency/suspend/suspend-vs-blocking`, `concurrency/shared-mutable-state/race-conditions-in-coroutines` and `concurrency/channels/channels-between-coroutines` — this branch comes first and gives the plain-thread picture; Concurrency re-uses it with coroutines.

Foundation is recommended first ("Start here"), then `kotlin`. Phase plan: what-an-os-does to threads in phase 3; scheduling to deadlock in phase 4; files to putting-it-together in phase 6.

Every theory leaf names where the idea shows up in an app in its Notes cell (`Android:`), as the Theory-Scared Student requires (`docs/01` §2.3). Anything about Android internals (Zygote, Binder, the low-memory killer, per-app memory limits, process importance) is `volatile` and carries `⚠️ VERIFY` against source.android.com or developer.android.com before the leaf goes live. No size, time, limit or ranking number is claimed in this roadmap.

Cited by other roadmaps, with the full leaf ID that should replace each stem reference when those roadmaps are next edited through `curriculum-designer`:

- `android-core` cites stem `os/processes` for who-starts-your-app and what-is-process-death: use `os/processes/what-is-a-process` (add `os/processes/process-states` for process-death).
- `android-core` cites stem `os/memory` for leaking-an-activity: use `os/memory/what-keeps-an-object-alive`.
- `android-core` cites stem `os/android-os` and stem `os/files`: use `os/android-os/low-memory-killer` and `os/files/files-folders-and-owners`.
- `concurrency` cites stem `os/threads`: use `os/threads/what-is-a-thread` for running-work-on-another-thread and `os/threads/a-thread-that-waits` for suspend-vs-blocking.
- `concurrency` cites stem `os/synchronisation`: use `os/synchronisation/race-conditions` for race-conditions-in-coroutines.
- `architecture-data` cites stem `os/files`: use `os/files/files-folders-and-owners` (what a file is) for rows-tables-and-keys.

## Stems

### what-an-os-does · What an Operating System Does

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-the-os-does-for-apps` | easy | "What does the operating system do for my app that my code cannot do alone?" | A building manager who hands out rooms, keys and electricity so tenants do not fight over them. | "My app talks to the screen, the camera and the storage by itself." | `none:` one picture of apps, the operating system and the hardware in three bands; the motion comes in the next leaf. | `P:` sort five jobs (share the CPU, draw pixels, keep apps apart, store files, run my lambda) into "OS does it" or "my code does it" · `B:` remove the OS band and ask who would share the screen | Android: why your app and the music app both play sound and use the screen without clashing. Assumes Foundation: `foundation/how-computers-work/what-is-an-os`, `foundation/how-computers-work/what-is-a-cpu`. |
| `kernel-and-system-calls` | easy | "How does my app ask the operating system to use the hardware for it?" | A bank counter behind glass: you pass a slip through the slot and staff do the work in the vault. | "My code runs straight on the hardware, so it can read any memory or file it likes." | `none:` one rule (apps stay on one side of a wall and pass requests through one door) drawn on a single diagram; the Playground cannot show a real system call. | `P:` which of four lines (add two numbers, read a file, print, open a socket) ends in a request to the operating system · `F:` a program that opens a missing file and ignores the error · `B:` ask for a file you have no right to | Android: a `SecurityException` or "permission denied" when an app reaches for something it was not given. ⚠️ VERIFY: Android uses the Linux kernel (source.android.com). Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu`. |

### processes · Processes

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-process` | easy | "Why does one crashing app leave all the other apps running?" | Flats in a block: each has its own door and lock, so a fire in one flat does not walk into the next. | "If one app crashes, the whole phone crashes." | `sim:` apps as sealed boxes on a table; the learner pops one box and the others stay; a hand reaching into another box bounces off the wall. | `P:` how many processes exist after the same program is started twice · `F:` a program that expects a second run to remember a counter from the first run · `B:` pop one box | Android: the "App has stopped" dialog while the rest of the phone keeps working. ⚠️ VERIFY: each app gets its own process by default and a manifest option can change that (developer.android.com). Assumes Foundation: `foundation/how-computers-work/what-is-an-os`. Helps to know Android core and lifecycles: `android-core/what-is-an-app/who-starts-your-app`. |
| `process-states` | medium | "What is a process doing while it waits for its turn?" | A clinic: a waiting room, a consulting room, a blood-test trip that sends you away until the result is back, and the exit door. | "A process is either running or finished." | `sim:` a token moves between four rooms (ready, running, waiting, done); the learner presses "needs the disk" and the token leaves the running room until the answer comes back. | `P:` the state of a process right after it asks for a file · `F:` a state table that lets a waiting process jump to running before its answer arrives · `B:` remove the answer and watch the token never return | Android: an app in the background still exists but runs no code; "waiting" is the state behind a not-responding dialog. ⚠️ VERIFY wording on the ANR page at developer.android.com; repeat no time limit here. Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu`. |
| `passing-data-between-processes` | medium | "How can two apps share data if neither can see the other's memory?" | Two flats that pass notes through a letterbox in the corridor: the note is copied, not shared. | "Two apps can read each other's variables if they ask nicely." | `sim:` two sealed boxes with a corridor and a post slot; a parcel is copied through the slot; a hand reaching straight in bounces off; a bigger parcel takes longer. | `P:` after the copy, does changing the sender's list change the receiver's list · `F:` a model where two "processes" share one mutable list and both change it · `B:` make the parcel bigger than the slot | Android: sharing a photo from the gallery to a chat app goes through the system, not through shared memory. Helps to know Android core and lifecycles: `android-core/intents/what-is-an-intent`. Pipes and files are named in one line as other ways; the Android way is `os/android-os/binder-and-system-services`. |

### memory · Memory

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `stack-and-heap` | medium | "Why do local variables disappear when a function ends but objects can live on?" | A desk and a warehouse: the desk holds the sheet you are working on and is cleared when you finish; the warehouse keeps labelled boxes until nobody needs them. | "Everything I declare lives in the same place, and an object sits inside its variable." | `sim:` a short pile of function sheets that grows and shrinks as functions are called and return, beside a shelf of labelled boxes; each variable is a string tied to a box. | `P:` which values are still reachable after a function returns · `F(ai):` a tidy recursive function over user input with no depth limit that overflows the stack · `F(ai):` a function that returns a reference to something it has built and then clears it · `B:` call the recursive function with a big number | Android: the crash names `StackOverflowError` and `OutOfMemoryError` point at these two places. ⚠️ VERIFY: the stack size per thread on Android; quote no number. Assumes Kotlin language: `kotlin/classes-and-objects/objects-are-references`, `kotlin/functions/declaring-functions`. Assumes Foundation: `foundation/programming-thinking/what-is-a-function`. Helps to know DSA: `dsa/recursion/the-call-stack`, `dsa/stacks/what-is-a-stack`. |
| `what-keeps-an-object-alive` | medium | "Why is an object I no longer need still using memory?" | A library book stays "on loan" while any member holds a card for it; one forgotten card keeps it out forever. | "Closing a screen or setting a variable to null frees the memory straight away, and the garbage collector fixes every leak." | `sim:` boxes tied to strings; a few fixed anchors at the top hold the strings; cutting the last string lets a box fall into the bin at the next sweep; a long string from a long-lived holder to a box keeps it up. | `P:` which boxes get swept after two strings are cut · `F(ai):` a tidy "helper" singleton that stores a reference to a screen · `F(ai):` a list of listeners that grows on every visit and is never cleared · `B:` cut the one string that matters | Android: the "leaked Activity" story: after many screens the app slows down or ends with `OutOfMemoryError`. ⚠️ VERIFY ART garbage-collection wording at source.android.com. Assumes Foundation: `foundation/from-code-to-running/what-is-art`. Assumes Kotlin language: `kotlin/classes-and-objects/objects-are-references`. Helps to know Android core and lifecycles: `android-core/lifecycle/leaking-an-activity` (it cites this stem). |
| `virtual-memory` | hard | "Why does every app think it has the whole memory to itself?" | Hotel room numbers: every guest is told "room 101" on their own floor, and reception knows which real room that is. | "The address my program sees is the real spot on the memory chip, so two apps can never use the same address." | `sim:` two apps list identical addresses on the left; a map table in the middle points each to a different real slot; an address with no entry hits a wall (a crash); the learner moves a slot out to storage and the app waits. | `P:` where address 5 of app A and of app B land · `F:` a model of the map table where two apps point to the same real slot and one overwrites the other · `B:` delete a map entry and run the app | Android: why one app can never read another app's data, and why each app has a memory limit. ⚠️ VERIFY: per-app heap limits, and whether phones page memory to storage or use compressed memory instead (source.android.com, developer.android.com). Assumes Foundation: `foundation/how-computers-work/memory-vs-storage`. This leaf absorbs the draft stem "virtual memory" (`docs/08` §9 trim). |

### threads · Threads

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-thread` | medium | "How is a thread different from a process?" | Cooks in one kitchen: they share the pantry and the stove, but each cook has a private notepad. | "A thread is a tiny process with its own private memory." | `sim:` a kitchen box (the process) with three cook balls; one shared pantry shelf and a private notepad per cook; a cook changes a pantry item and the others see it at once. | `P:` which values another thread can see after one thread changes a shared variable and a local one · `F:` a program where two threads use one list and the second thread's work is wrongly assumed to be private · `B:` add a fourth cook | Android: the main thread and the background threads inside one app share the app's objects. ⚠️ VERIFY that the Playground runs plain `thread { }`. Assumes Kotlin language: `kotlin/lambdas/lambda-syntax`. Helps to know Concurrency: `concurrency/why-the-main-thread/what-the-main-thread-does`. Cited by `concurrency` as stem `os/threads`. |
| `what-a-thread-costs` | easy | "Why not start a new thread for every small job?" | Hiring a full-time employee for each five-minute errand: the desk, the pay and the training come before any work. | "Threads are free, so more threads always means a faster app." | `none:` a small table of what each thread costs (a private notepad, time to start, time to switch) and a Playground program that starts 1, 100 and 1000 threads and prints the time. | `P:` does a program with 1000 threads that each wait 1 second finish in about 1 second or 1000 seconds · `F:` a loop that starts one thread per item for 100,000 items · `B:` start 10,000 threads | Android: an app that creates a thread per image or per tap can end with a "could not create thread" crash; thread pools exist for this. ⚠️ VERIFY exact error text and any limit before saying more. Helps to know Concurrency: `concurrency/threads/why-raw-threads-get-messy`, `concurrency/coroutine-basics/what-is-a-coroutine`. |
| `a-thread-that-waits` | medium | "What happens to a thread while it waits for a file or a reply?" | Someone on hold on the phone: they can do nothing else, yet they burn no energy. | "A waiting thread keeps the CPU busy." | `sim:` reuse `os/threads/what-is-a-thread` sim; one cook is stuck on a phone line, the CPU meter drops while that cook's task bar stalls; other cooks carry on; if the stuck cook is the only cook, the whole line stops. | `P:` how long a program takes when it waits 2 seconds three times on one thread versus on three threads · `F:` a click-handler model that waits for a reply on the only thread · `B:` add a second thread for the wait | Android: why a network call on the main thread freezes the screen; Android may stop you with `NetworkOnMainThreadException`. ⚠️ VERIFY the name and when it is thrown at developer.android.com. This is the picture behind `concurrency/suspend/suspend-vs-blocking`. |

### scheduling · Scheduling

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `one-cpu-many-apps` | easy | "How can one CPU core run ten apps at the same time?" | One teacher answering ten students by giving each a minute at a time, so quickly that it feels like all at once. | "Ten open apps means ten things running at the same moment on one core." | `sim:` a carousel of ten app balls and one counter; a slider sets the time slice; each switch costs a small pause shown as dead time; very short slices waste time on switching. | `P:` how many turns each of three tasks gets in 12 ticks with a slice of 2 · `F:` a round-robin model that never puts an unfinished task back in line · `B:` set the slice to 1 and count the wasted time | Android: why a heavy job in one app can make another app lag. Assumes Foundation: `foundation/how-computers-work/what-is-a-cpu`. Helps to know DSA: `dsa/queues/what-is-a-queue`. |
| `priorities-and-starvation` | easy | "Why can a low-priority job wait forever?" | An airport boarding line where priority passengers always go first, so a new priority passenger keeps arriving ahead of you. | "Priority only decides how soon a job finishes; every job still gets a turn." | `sim:` reuse `os/scheduling/one-cpu-many-apps` sim with two lanes; a stream of high-priority balls keeps one low-priority ball waiting; an "age bonus" switch lifts the waiting ball. | `P:` the order out for jobs with priorities 3, 1, 2, 3 · `F:` a scheduler model that always picks the highest priority and never raises a waiting job · `B:` add a steady stream of urgent jobs | Android: the app you are using is served before apps in the background, and background work may finish late. ⚠️ VERIFY how Android groups apps for scheduling (developer.android.com, source.android.com). Helps to know DSA: `dsa/heaps/priority-queue-idea`. |

### synchronisation · Synchronisation

Flags: phase=4 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `race-conditions` | hard | "Why can two threads updating one counter lose an update?" | Two cashiers selling the last ticket from one paper list: both read "1 left", both sell, and the list shows 0 after two sales. | "`counter++` is one step, so nothing can come between." | `sim:` two hands read a shared jar, add one in their palm and put it back; the learner drags the switch point between the hands to choose the order of steps and watches an update vanish. | `P:` the possible final values of a counter after two threads add 1 each · `F(ai):` a tidy parallel counter built on a plain `var` · `F(ai):` "check, then act" on a shared flag with a gap between the two · `B:` pick the switch point that loses an update | Android: a double-tap on a Pay button sends two orders; a counter or list is changed from many background threads. Assumes Kotlin language: `kotlin/lambdas/closures`, `kotlin/types-and-variables/val-vs-var`. Helps to know Concurrency: `concurrency/shared-mutable-state/race-conditions-in-coroutines`. Cited by `concurrency` as stem `os/synchronisation`. |
| `locks-and-critical-sections` | medium | "How does a lock let only one thread into a risky piece of code?" | A changing room with one key on a hook: the holder goes in, the rest queue; if the holder takes the key home, nobody changes. | "Locking everything makes code safe at no cost." | `sim:` reuse `os/synchronisation/race-conditions` sim with a key on a hook; with the key one hand works at a time; a long hold grows the queue; a key never returned stalls all. | `P:` the final counter value with and without a lock · `F(ai):` a lock that covers the read but not the write · `F(ai):` a lock held around a slow network call · `B:` hold the key for five seconds | Android: a lock held on the main thread can freeze the screen. ⚠️ VERIFY which locking tools exist in Kotlin common code (kotlinlang.org) before naming any. Helps to know Concurrency: `concurrency/shared-mutable-state/race-conditions-in-coroutines`. |
| `producer-and-consumer` | medium | "How can one thread hand work to another without wasting CPU?" | A sushi belt with five plates: the chef stops when the belt is full, the guest waits when it is empty. | "The receiver should keep checking the box in a loop until something arrives." | `sim:` a conveyor belt of five slots; the learner sets chef speed and guest speed; a full belt pauses the chef, an empty belt pauses the guest; a "keep checking" switch makes the guest burn a CPU meter. | `P:` the items on the belt after 6 ticks when the chef is faster · `F(ai):` a polling loop that spins until a list is non-empty · `F(ai):` a queue with no size limit that grows until memory runs out · `B:` make the belt size 1 | Android: the main thread takes messages from a queue one by one; downloads and logs wait in queues the same way. ⚠️ VERIFY wording about the main thread's message queue on developer.android.com. Helps to know DSA: `dsa/queues/what-is-a-queue`. Helps to know Concurrency: `concurrency/channels/channels-between-coroutines`. |

### deadlock · Deadlock

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-deadlock` | hard | "How can two threads wait for each other forever?" | Two cars meet on a one-lane bridge, and each waits for the other to back out. | "A frozen app is always stuck in an endless loop." | `sim:` four cars at a crossing, each needing the square held by the next car; four switches (one at a time, hold and wait, no taking away, a circle of waiting) and turning any one off clears the jam. | `P:` whether a given pair of lock orders can deadlock · `F(ai):` two functions that take the same two locks in opposite order · `F(ai):` a callback that takes a lock while its caller already holds it · `B:` change the order of one lock | Android: a screen that freezes with no heavy loop in sight and ends in a not-responding dialog. ⚠️ VERIFY wording on the ANR page at developer.android.com. Names the "always take locks in the same order" fix. |

### files · Files

Flags: phase=6 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `files-folders-and-owners` | easy | "Why can my app open some folders but not others?" | A library where every shelf carries a label saying who may read or change it, and the librarian checks your card. | "Any app can open any file on the phone if it knows the path." | `none:` a folder tree with an owner tag and read or write marks on each door; real file rules cannot be shown by code in the browser editor. | `P:` which of five paths an app with its own folder can open · `F:` a path built by hand that points outside the app's folder · `B:` change the owner tag on a door | Android: each app has its own private folder that other apps cannot read; the Files app shows only shared folders. ⚠️ VERIFY: Android gives each app its own Linux user (source.android.com security pages). Assumes Foundation: `foundation/how-computers-work/memory-vs-storage`. Helps to know Android core and lifecycles: `android-core/files-and-sharing/app-private-files`. Helps to know DSA: `dsa/trees/what-is-a-tree`. Cited by `android-core` and `architecture-data` as stem `os/files`. |
| `why-disk-is-slow` | easy | "Why is reading a file so much slower than reading a variable?" | Fetching a pen from your pocket, from a cupboard across the room, or from a shop across town. | "Storage is as fast as memory, only bigger." | `none:` a table of the same read at three distances scaled to human time (pocket, cupboard, shop across town) plus a Playground program that prints the time of reading a list in memory versus reading a file; a moving picture adds nothing the table does not. | `P:` which of three reads finishes first · `F:` a loop that re-reads the same file for each of 1,000 lines · `B:` add the cache shelf | Android: slow disk reads on the main thread make list screens stutter, and StrictMode can flag them. ⚠️ VERIFY StrictMode wording at developer.android.com; quote no speed numbers without a checked source. Assumes Foundation: `foundation/how-computers-work/memory-vs-storage`. |

### android-os · Android Under the App

Flags: phase=6 · ai-review · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `zygote-and-app-processes` | medium | "How does Android create an app process so quickly?" | A bakery that keeps a tray of half-baked rolls ready; each order copies the tray and adds its own topping instead of starting from flour. | "Every app loads the whole Android system from scratch when it starts." | `sim:` reuse `os/processes/what-is-a-process` sim, with a ready-made template box; pressing "open app" copies it into a new box that shares the unchanged parts until the app writes to them. | `P:` how many copies of the shared system code sit in memory for three apps · `F(ai):` a start-up model that repeats the same loading work for every app and so is slow · `B:` stop the template and open an app | Android: why a cold start takes longer than showing an app already in memory. ⚠️ VERIFY Zygote, fork and copy-on-write wording at source.android.com. Assumes Android core and lifecycles: `android-core/what-is-an-app/who-starts-your-app`, `android-core/app-startup/cold-warm-hot-start`. Assumes Foundation: `foundation/from-code-to-running/what-is-art`. |
| `binder-and-system-services` | medium | "How does my app call a system service that lives in another process?" | A hotel switchboard: you dial the front desk, the operator connects you, and a copy of the answer comes back. | "`getSystemService` gives me an object that lives inside my app and runs in my process." | `sim:` reuse `os/processes/passing-data-between-processes` sim; the slot is now a switchboard that carries one parcel at a time and rejects a parcel that is too big. | `P:` which calls in a short screen cross a process boundary · `F(ai):` a screen that sends a whole list of images between two screens in one parcel · `B:` shrink the parcel | Android: `TransactionTooLargeException` when a screen passes too much data; location, notifications and the clipboard are all asked of other processes. ⚠️ VERIFY exception name and the shared size limit on developer.android.com. Helps to know Android core and lifecycles: `android-core/intents/what-is-an-intent`. |
| `low-memory-killer` | medium | "Why did Android close my app while I was away?" | A lifeboat with ranked seats: when it overloads, the least important passenger leaves first and the one steering stays. | "The phone only closes apps that misbehave." | `sim:` a RAM bar fills as app balls arrive; when it is full the lowest-ranked ball is removed; the learner pins one app in front and moves between screens as the rank colours change. | `P:` which of four apps is removed first · `F(ai):` a screen that keeps its only copy of a draft in a variable · `B:` open six apps and watch the ranking | Android: the app returns with its screen reset, which is process death. ⚠️ VERIFY the name of the low-memory killer and the process-importance list at developer.android.com and source.android.com. Assumes Android core and lifecycles: `android-core/process-death/what-is-process-death`, `android-core/background-work/why-background-is-limited`. Cited by `android-core` as stem `os/android-os`. |

### putting-it-together · Putting It Together

Flags: phase=6 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `match-symptoms-to-causes` | hard | "Which operating-system event explains this app symptom?" | A mechanic's checklist: a car that will not start has a few usual causes, and you test them in order. | "Every freeze or crash is a bug in my own code." | `none:` a matching exercise on five bug reports; the earlier sims are the material. | `P:` match five symptoms (a freeze with no heavy loop, a slow list, an app closed in the background, `StackOverflowError`, `OutOfMemoryError` after many screens) to their causes · `F(ai):` a generated "fix" that adds more threads to cure a deadlock · `B:` pick a wrong cause and read what the evidence would show | Android: the `docs/01` §6.2 check "match 5 symptoms to causes". Assumes Android core and lifecycles: `android-core/lifecycle/leaking-an-activity`. Assumes Concurrency: `concurrency/why-the-main-thread/why-blocking-freezes-the-screen`. Branch synthesis; fruit (last leaf, `docs/08` §12 F2). |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `os/putting-it-together/match-symptoms-to-causes` | Symptom casebook | At least five symptoms from apps you have really used or built · each names the operating-system idea (a leaf ID of this branch) that explains it · each names the evidence that would show it (a log line, a crash name, a screen recording) · two symptoms reproduced as small plain-Kotlin models you ran · at least one symptom that looks like a code bug but is not · one page, no new library | "Which OS event causes a given app symptom" (`docs/01` §6.2, check: match 5 symptoms to causes; L24). Last leaf of the branch; the only fruit, because no segment end sits 15 leaves from it (F3). |

## Later

- Page-replacement algorithms: a theory detail; virtual-memory only needs the idea of a map.
- File-system internals (inodes, journaling): a specialist topic; the files stem only needs owners and speed.
- Advanced scheduling (multi-level feedback queues): exam theory; one-cpu-many-apps covers turns, slices and priorities.
- Semaphores versus monitors theory: locks and the producer belt cover what an app writer needs.
- Interrupts and device drivers: below what an app developer touches.
- Memory allocators (how a memory allocator works): library-author topic.
- Containers and virtual machines: server topic, not Android.
- Linux shell scripting: belongs with tools, if ever.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 10 stems, 23 leaves, all `shadow`. | +23 | Estimate 22, band 18–26: inside the band (+5 %). Stems and sizes follow the `docs/08` §9 sketch except: `putting-it-together` (1 leaf) added so the last leaf is the symptom-matching synthesis leaf and the fruit proves the `docs/01` §6.2 ability (F2); `processes` and `memory` are placed before `threads` because a thread is defined against a process and a private stack. `process-states` is its own leaf because the ANR story and `a-thread-that-waits` need it. Virtual memory is one leaf inside `memory` (trim, `docs/08` §9). |
| 2026-10-18 | Difficulty mix: 8 easy, 11 medium, 4 hard (17 % hard). New sims: 12 of 23 leaves (52 %), plus 5 reuse of an earlier sim and 6 `none:`. | 0 | Medium-heavy because many leaves hide timing or memory behaviour (Q2) and are commonly misread (Q4). Hard share is under 30 %. New-sim share sits slightly above the 35–50 % guide for OS because processes, memory, scheduling and synchronisation are all about time and order; the five reuse sims keep the real workload lower. |
