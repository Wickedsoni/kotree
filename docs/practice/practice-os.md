# practice: os

## os/what-an-os-does/what-the-os-does-for-apps
problems: none
interview-questions:
- Explain it aloud: What would happen if two Android apps tried to write pixels directly to the display hardware or read frames from the camera sensor without an operating system mediating access?
- Explain it aloud: What are the four primary resources the operating system virtualizes and arbitrates for running applications (CPU, memory, disk/files, and I/O devices)?
- Explain it aloud: Why is an Android app forbidden from executing privileged CPU instructions directly, and how does hardware protection (user mode vs kernel mode) enforce that boundary?
- Explain it aloud: When your Kotlin code calls `FileInputStream.read()` or opens a network socket, where does your app code stop and the operating system's responsibility begin?

## os/what-an-os-does/kernel-and-system-calls
problems: none
interview-questions:
- Explain it aloud: Walk step-by-step through what happens on the CPU when user-space code triggers a system call (`read`, `write`, `mmap`, `ioctl`)—how does the processor switch from User Mode to Kernel Mode and back?
- Explain it aloud: Why is a system call significantly more expensive than a normal Kotlin/Java function call even when the kernel does very little work inside the call?
- Explain it aloud: Why does standard library I/O (such as `BufferedInputStream` or `BufferedOutputStream`) buffer reads and writes in user space instead of issuing a `read()` or `write()` system call for every single byte?
- Explain it aloud: On Android, how do JNI calls and `libc` (Bionic) sit between your Kotlin code and the Linux kernel's system call table?

## os/processes/what-is-a-process
problems: none
interview-questions:
- Explain it aloud: What is the difference between a static program (an installed APK on disk) and a running OS process, and what data does the kernel store inside a Process Control Block (`task_struct` in Linux)?
- Explain it aloud: Why does a `NullPointerException` or native `SIGSEGV` crash in one Android app leave the system launcher, music player, and other apps running untouched?
- Explain it aloud: How does Android use Linux User IDs (UIDs) to isolate each installed app into its own process sandbox by default?
- Explain it aloud: When is it useful or dangerous to declare `android:process=":remote"` in `AndroidManifest.xml` to run a Service or WebView in a separate OS process?

## os/processes/process-states
problems:
- LeetCode 636 | Exclusive Time of Functions | Medium | https://leetcode.com/problems/exclusive-time-of-functions/ | Tracks running vs preempted/suspended execution intervals on a single CPU timeline
- LeetCode 582 | Kill Process | Medium | https://leetcode.com/problems/kill-process/ | Models a parent-child PID tree where terminating a parent transitions its entire subtree to terminated
- LeetCode 621 | Task Scheduler | Medium | https://leetcode.com/problems/task-scheduler/ | Transitions tasks between Ready, Running, and Waiting (cooldown) states across discrete CPU ticks
- LeetCode 1834 | Single-Threaded CPU | Medium | https://leetcode.com/problems/single-threaded-cpu/ | Simulates tasks moving from arrival into a Ready queue, onto a Running CPU, and finally to completion
interview-questions:
- Draw or describe the 5-state process lifecycle (New, Ready, Running, Waiting/Blocked, Terminated) and name the exact event that triggers each transition.
- What is the difference between a process in the **Ready** state and a process in the **Waiting/Blocked** state? Does either one consume CPU cycles while waiting?
- What is a **context switch**, what CPU registers and state must the kernel save and restore during one, and why does excessive context switching hurt throughput?
- On Linux/Android, what is a **zombie process** (or defunct state), and how does a parent process clean it up?

## os/processes/passing-data-between-processes
problems:
- LeetCode 271 | Encode and Decode Strings | Medium | https://leetcode.com/problems/encode-and-decode-strings/ | Implements length-prefixed framing to marshal and unmarshal strings across an IPC byte stream or pipe
- LeetCode 449 | Serialize and Deserialize BST | Medium | https://leetcode.com/problems/serialize-and-deserialize-bst/ | Marshals a pointer-based tree into a compact wire representation that another process can reconstruct
- LeetCode 138 | Copy List with Random Pointer | Medium | https://leetcode.com/problems/copy-list-with-random-pointer/ | Demonstrates why raw memory pointers cannot simply be copied into another address space without remapping
- LeetCode 297 | Serialize and Deserialize Binary Tree | Hard | https://leetcode.com/problems/serialize-and-deserialize-binary-tree/ | Full serialization and deserialization of arbitrary object graphs across address-space boundaries
interview-questions:
- Why can't Process A simply pass a Kotlin object reference (a memory address like `0x7ff8a040`) to Process B for Process B to dereference?
- Compare the main Inter-Process Communication (IPC) mechanisms—pipes, Unix domain sockets, shared memory (`ashmem` / `memfd`), and Android Binder—in terms of how many times data is copied in memory.
- On Android, why is `Parcelable` dramatically faster for IPC than `java.io.Serializable`?
- If two processes use shared memory (`mmap`) to avoid copying a large camera frame or bitmap, what new concurrency problem do they immediately have to solve?

## os/memory/stack-and-heap
problems:
- LeetCode 20 | Valid Parentheses | Easy | https://leetcode.com/problems/valid-parentheses/ | Models strict LIFO allocation and deallocation where the most recently opened scope must close first
- LeetCode 394 | Decode String | Medium | https://leetcode.com/problems/decode-string/ | Contrasts call-stack frame growth during recursion with heap-allocated dynamic buffers
- LeetCode 636 | Exclusive Time of Functions | Medium | https://leetcode.com/problems/exclusive-time-of-functions/ | Simulates function stack frames pushing onto and popping off a thread's call stack
- LeetCode 138 | Copy List with Random Pointer | Medium | https://leetcode.com/problems/copy-list-with-random-pointer/ | Allocates independent nodes on the heap whose lifetimes outlive the stack variables used to wire them
interview-questions:
- Why is allocating a local primitive variable on the **stack** just a single CPU instruction (moving the stack pointer), whereas allocating an object on the **heap** is much more complex?
- If a Kotlin function creates `val user = User("Asha")` inside a method, where does the reference `user` live, where does the `User` object live, and what happens to each when the function returns?
- Does each thread in a process have its own stack, its own heap, or both? How does that affect thread safety for local variables vs object fields?
- What is the difference between `StackOverflowError` and `OutOfMemoryError`, and what kind of code bug triggers each one in an Android app?

## os/memory/what-keeps-an-object-alive
problems:
- LeetCode 141 | Linked List Cycle | Easy | https://leetcode.com/problems/linked-list-cycle/ | Illustrates reference cycles that break naive reference counting (`retainCount` never hits 0) but are collected by tracing GC
- LeetCode 841 | Keys and Rooms | Medium | https://leetcode.com/problems/keys-and-rooms/ | Traces transitive reachability from a root node (room 0) to identify unreachable "garbage" nodes
- LeetCode 547 | Number of Provinces | Medium | https://leetcode.com/problems/number-of-provinces/ | Models disconnected object components in a reference graph where only components tied to a GC root stay alive
- LeetCode 133 | Clone Graph | Medium | https://leetcode.com/problems/clone-graph/ | Traverses a cyclic object graph from a root reference using a visited set, mirroring the Mark phase of Mark-and-Sweep GC
interview-questions:
- How does a tracing Garbage Collector (like Android Runtime's ART GC) decide whether an object on the heap is still alive or eligible for collection, and what are **GC Roots**?
- Why can two objects that reference each other in a cycle still be garbage-collected in Kotlin/Java (tracing GC), whereas they would leak in unassisted reference counting (like Swift/Objective-C without `weak`)?
- Give three classic ways an Android developer accidentally leaks a destroyed `Activity` or `Fragment` by keeping a path from a GC Root alive.
- What is the difference between a strong reference, a `WeakReference`, and a `SoftReference` on the JVM/Android, and why are generational GCs divided into Young and Old generations?

## os/memory/virtual-memory
problems:
- LeetCode 622 | Design Circular Queue | Medium | https://leetcode.com/problems/design-circular-queue/ | Models fixed-frame ring buffers used in FIFO and Clock page-replacement algorithms
- LeetCode 146 | LRU Cache | Medium | https://leetcode.com/problems/lru-cache/ | Implements the Least Recently Used eviction policy used to model OS page replacement and hardware TLB caches
- LeetCode 460 | LFU Cache | Hard | https://leetcode.com/problems/lfu-cache/ | Implements frequency-based eviction in O(1) to contrast recency vs frequency page replacement
- LeetCode 715 | Range Module | Hard | https://leetcode.com/problems/range-module/ | Tracks mapped and unmapped contiguous intervals, mirroring how a kernel manages virtual memory address ranges (`mmap` / `munmap`)
interview-questions:
- How do the CPU's Memory Management Unit (MMU) and the kernel's Page Tables translate a virtual memory address in your app into a physical RAM address?
- What is a **Page Fault** (minor/soft fault vs major/hard fault), and what happens when your app touches a memory-mapped (`mmap`) file page that is not yet in physical RAM?
- What is the **TLB (Translation Lookaside Buffer)**, and why would virtual memory make every RAM access at least twice as slow without a TLB?
- Why does LRU (Least Recently Used) approximate the theoretical optimal page replacement algorithm (Bélády's MIN), and what is **thrashing** when working-set memory exceeds physical RAM?

## os/threads/what-is-a-thread
problems:
- LeetCode 1114 | Print in Order | Easy | https://leetcode.com/problems/print-in-order/ | Coordinates three threads executing different methods on the same shared heap object instance
- LeetCode 1115 | Print FooBar Alternately | Medium | https://leetcode.com/problems/print-foobar-alternately/ | Two threads sharing an instance inside the same process and alternating turns
- LeetCode 1195 | Fizz Buzz Multithreaded | Medium | https://leetcode.com/problems/fizz-buzz-multithreaded/ | Four threads cooperating over a shared counter `1..n` within a single address space
- LeetCode 1242 | Web Crawler Multithreaded | Medium | https://leetcode.com/problems/web-crawler-multithreaded/ | Multiple worker threads sharing a concurrent visited-URL set in process memory to parallelize I/O
interview-questions:
- Which OS resources do two threads inside the **same process** share (heap, global/static variables, open file descriptors, code segment), and what does each thread own privately (program counter, registers, call stack)?
- Why is switching between two threads of the same process cheaper for the CPU than switching between two different processes?
- In an Android app, what is special about the **Main (UI) Thread**, and why does Android throw `CalledFromWrongThreadException` if a background thread mutates a `View` directly?
- Why does an uncaught exception on *any* background thread crash the entire Android process by default?

## os/threads/what-a-thread-costs
problems: none
interview-questions:
- Explain it aloud: How much virtual stack memory does a single JVM/Android OS thread reserve by default (typically ~1 MB), and what happens if your app spawns 1,000 raw `Thread { ... }.start()` instances at once?
- Explain it aloud: Beyond stack memory, what kernel and CPU costs are paid when creating, scheduling, and context-switching OS threads?
- Explain it aloud: How does a **Thread Pool** (`Executors.newFixedThreadPool` or `Dispatchers.IO` / `Dispatchers.Default`) amortize the cost of thread creation and cap maximum concurrency?
- Explain it aloud: Why can you launch 100,000 Kotlin **coroutines** in a few megabytes of heap memory when launching 100,000 OS threads would immediately crash the app with `OutOfMemoryError`?

## os/threads/a-thread-that-waits
problems:
- LeetCode 1114 | Print in Order | Easy | https://leetcode.com/problems/print-in-order/ | Contrasts spinning in a `while` loop (burning 100% CPU) with blocking on a `CountDownLatch`, `Semaphore`, or condition variable
- LeetCode 1116 | Print Zero Even Odd | Medium | https://leetcode.com/problems/print-zero-even-odd/ | Parks two threads in a waiting state while the third thread runs, waking the exact thread needed next
- LeetCode 1117 | Building H2O | Medium | https://leetcode.com/problems/building-h2o/ | Blocks surplus hydrogen or oxygen threads until a complete 2H + 1O barrier can be released together
- LeetCode 1188 | Design Bounded Blocking Queue | Medium | https://leetcode.com/problems/design-bounded-blocking-queue/ | Puts calling threads to sleep when the queue is full or empty and wakes them when space or data arrives
interview-questions:
- What is the difference between **busy-waiting (spinning)** and **blocking (sleeping)**, and when is a spinlock ever preferable to putting a thread to sleep?
- What happens inside the OS scheduler when a thread calls a blocking socket `read()` or `Object.wait()`? How does the thread wake back up when the network packet arrives (hardware interrupt -> ready queue)?
- Why must `wait()` / `Condition.await()` ALWAYS be called inside a `while (!condition)` loop rather than an `if (!condition)` statement (spurious wakeups and stolen conditions)?
- In Kotlin coroutines, how does `delay(1000)` or a suspending Retrofit call wait without blocking the underlying OS thread (`Thread.sleep(1000)`)?

## os/scheduling/one-cpu-many-apps
problems:
- LeetCode 621 | Task Scheduler | Medium | https://leetcode.com/problems/task-scheduler/ | Schedules competing tasks across discrete time slices on a single CPU while minimizing idle ticks
- LeetCode 636 | Exclusive Time of Functions | Medium | https://leetcode.com/problems/exclusive-time-of-functions/ | Computes actual on-CPU time when tasks preempt and resume on a single-core timeline
- LeetCode 767 | Reorganize String | Medium | https://leetcode.com/problems/reorganize-string/ | Interleaves high-frequency jobs so no single job monopolizes back-to-back slots
- LeetCode 1834 | Single-Threaded CPU | Medium | https://leetcode.com/problems/single-threaded-cpu/ | Simulates non-preemptive Shortest-Job-First (SJF) scheduling on a single CPU core using a Min-Heap
interview-questions:
- What is the difference between **concurrency** (interleaving progress via time-slicing on 1 core) and **parallelism** (simultaneous execution across multiple physical cores)?
- Compare **First-Come, First-Served (FCFS)**, **Shortest Job First (SJF)**, and **Round-Robin** scheduling in terms of average turnaround time, response time/interactivity, and the "convoy effect".
- In Round-Robin scheduling, what happens if the time slice (quantum) is set way too long (say, 500 ms) versus way too short (say, 1 microsecond)?
- How does an Android device keep a 60 Hz or 120 Hz UI animation smooth (`< 16.6 ms` or `< 8.3 ms` per frame) while background apps are also competing for CPU time?

## os/scheduling/priorities-and-starvation
problems:
- LeetCode 1834 | Single-Threaded CPU | Medium | https://leetcode.com/problems/single-threaded-cpu/ | Demonstrates how Shortest-Job-First priority ordering can force a long task to wait behind a stream of short tasks
- LeetCode 621 | Task Scheduler | Medium | https://leetcode.com/problems/task-scheduler/ | Uses a Max-Heap to prioritize tasks with the highest remaining work count
- LeetCode 1882 | Process Tasks Using Servers | Medium | https://leetcode.com/problems/process-tasks-using-servers/ | Assigns incoming tasks across weighted servers using dual priority queues for free and busy resources
- LeetCode 502 | IPO | Hard | https://leetcode.com/problems/ipo/ | Two-heap priority scheduling where runnable tasks are unlocked by resource thresholds and selected by highest priority
interview-questions:
- What is **starvation** in priority scheduling or Shortest-Job-First scheduling, and how does **aging** (gradually boosting a waiting task's priority over time) prevent it?
- What is **priority inversion** (where a high-priority UI thread waits on a lock held by a low-priority thread that gets preempted by medium-priority threads), and how does **priority inheritance** solve it?
- How does Linux's Completely Fair Scheduler (CFS) use `vruntime` (virtual runtime) in a Red-Black Tree so no runnable task starves?
- On Android, why are foreground UI threads placed in different `cgroups` / `nice` priority levels than background `WorkManager` threads?

## os/synchronisation/race-conditions
problems:
- LeetCode 1114 | Print in Order | Easy | https://leetcode.com/problems/print-in-order/ | Shows how uncoordinated threads execute in non-deterministic OS schedule order unless synchronized
- LeetCode 1115 | Print FooBar Alternately | Medium | https://leetcode.com/problems/print-foobar-alternately/ | Prevents interleaved race output (`"foofoo"` or `"barfoo"`) when two threads race to print
- LeetCode 1195 | Fizz Buzz Multithreaded | Medium | https://leetcode.com/problems/fizz-buzz-multithreaded/ | Prevents four threads from reading and incrementing a shared counter `i` out of turn
- LeetCode 1242 | Web Crawler Multithreaded | Medium | https://leetcode.com/problems/web-crawler-multithreaded/ | Fixes check-then-act races on a shared `visited` set when multiple threads discover the same URL simultaneously
interview-questions:
- Why is `count++` NOT an atomic operation at the CPU instruction level, and how can two threads executing `count++` once each starting from `0` end up with `count == 1`?
- What is the difference between **visibility** (`@Volatile` / CPU cache coherency) and **atomicity** (`AtomicInteger` / `synchronized`), and why does `@Volatile var count = 0` NOT make `count++` thread-safe?
- What is a **check-then-act** race condition (for example, `if (!map.containsKey(k)) map[k] = create()`), and how does `ConcurrentHashMap.computeIfAbsent` fix it?
- How does hardware **Compare-And-Swap (CAS)** allow `AtomicInteger.incrementAndGet()` to update a counter safely across threads without acquiring a heavy OS lock?

## os/synchronisation/locks-and-critical-sections
problems:
- LeetCode 1279 | Traffic Light Controlled Intersection | Easy | https://leetcode.com/problems/traffic-light-controlled-intersection/ | Protects shared intersection state (which road currently has the green light) inside a mutually exclusive critical section
- LeetCode 1115 | Print FooBar Alternately | Medium | https://leetcode.com/problems/print-foobar-alternately/ | Uses a mutex + condition variable or semaphores to guard a two-thread critical section
- LeetCode 1116 | Print Zero Even Odd | Medium | https://leetcode.com/problems/print-zero-even-odd/ | Coordinates mutual exclusion and turn signaling across three threads
- LeetCode 1226 | The Dining Philosophers | Medium | https://leetcode.com/problems/the-dining-philosophers/ | Protects pairs of shared fork resources with fine-grained locks so adjacent philosophers never eat simultaneously
interview-questions:
- What is a **critical section**, and what is the difference between a **Mutex (`synchronized` / `ReentrantLock`)** and a **Counting Semaphore (`Semaphore(k)`)**?
- What does it mean for a lock to be **reentrant**, and what would happen if a `synchronized` method called another `synchronized` method on the same object (`this`) using a non-reentrant lock?
- Why should you keep critical sections as short as possible, and why is performing network or disk I/O while holding a lock shared with the Android Main Thread a recipe for an ANR (Application Not Responding)?
- When does a **Read-Write Lock (`ReentrantReadWriteLock`)** outperform a standard exclusive lock, and when does its overhead make it slower?

## os/synchronisation/producer-and-consumer
problems:
- LeetCode 622 | Design Circular Queue | Medium | https://leetcode.com/problems/design-circular-queue/ | Builds the underlying bounded ring buffer (`head`, `tail`, `count`, `capacity`) used by producer-consumer queues
- LeetCode 1188 | Design Bounded Blocking Queue | Medium | https://leetcode.com/problems/design-bounded-blocking-queue/ | Textbook multi-producer multi-consumer bounded buffer using a lock and `notFull` / `notEmpty` condition variables (or two semaphores)
- LeetCode 1117 | Building H2O | Medium | https://leetcode.com/problems/building-h2o/ | Coordinates producer threads supplying H and O atoms at uneven rates into balanced 3-atom batches
- LeetCode 1242 | Web Crawler Multithreaded | Medium | https://leetcode.com/problems/web-crawler-multithreaded/ | Worker threads act as both consumers of queued URLs and producers of newly discovered child URLs
interview-questions:
- Walk through implementing a `BoundedBlockingQueue(capacity)` using `ReentrantLock` and two `Condition` objects (`notFull` and `notEmpty`). Why is using two separate `Condition`s better than calling `notifyAll()` on a single monitor?
- Why must a bounded producer-consumer queue have a maximum capacity (backpressure) in production systems? What happens if a fast producer feeds an unbounded queue while the consumer is slow?
- How does Android's `Looper` / `Handler` / `MessageQueue` implement the producer-consumer pattern between background worker threads (producers) and the Main UI thread (consumer)?
- In Kotlin Coroutines, how does a `Channel(capacity)` act as a non-blocking producer-consumer queue (`send` and `receive`)?

## os/deadlock/what-is-a-deadlock
problems:
- LeetCode 207 | Course Schedule | Medium | https://leetcode.com/problems/course-schedule/ | Detects cycles in a directed dependency graph—the exact algorithm an OS uses on a resource Wait-For Graph to detect deadlocks
- LeetCode 802 | Find Eventual Safe States | Medium | https://leetcode.com/problems/find-eventual-safe-states/ | Separates states that inevitably enter a dependency cycle (deadlock) from states guaranteed to terminate
- LeetCode 1226 | The Dining Philosophers | Medium | https://leetcode.com/problems/the-dining-philosophers/ | The canonical OS deadlock problem: breaks Circular Wait or Hold-and-Wait so 5 philosophers sharing 5 forks never freeze
- LeetCode 1188 | Design Bounded Blocking Queue | Medium | https://leetcode.com/problems/design-bounded-blocking-queue/ | Trains releasing the mutex cleanly while awaiting a condition so producers and consumers never deadlock each other
interview-questions:
- Name and explain **Coffman's four necessary conditions** for a deadlock to occur (Mutual Exclusion, Hold and Wait, No Preemption, Circular Wait).
- Why is **Lock Ordering** (for example, always acquiring `lock(min(idA, idB))` before `lock(max(idA, idB))`) the most practical way to prevent deadlocks in production code, and which of the four Coffman conditions does it break?
- In `The Dining Philosophers`, what happens if all 5 philosophers pick up their left fork at the exact same millisecond, and what are three distinct ways to prevent that deadlock?
- What is the difference between a **deadlock**, a **livelock**, and **thread starvation**? Give a real-world Android example of a deadlock involving `runBlocking` or `.get()` on the Main Thread.

## os/files/files-folders-and-owners
problems: none
interview-questions:
- Explain it aloud: What is an **inode** in a Linux/Android filesystem, and why is a file's name stored inside the parent directory entry rather than inside the inode itself?
- Explain it aloud: What do the three permission groups (`Owner / User`, `Group`, `Others`) and the three bits (`r`, `w`, `x`) mean for a regular file versus for a **directory**?
- Explain it aloud: Why can your Android app freely read and write `/data/data/<your.package.name>/files` without requesting any runtime permission, yet gets `EACCES (Permission denied)` if it tries to open another app's `/data/data/` folder?
- Explain it aloud: What is a **File Descriptor (FD)** in Linux, and what causes an Android app to crash with `IOException: Too many open files`?

## os/files/why-disk-is-slow
problems: none
interview-questions:
- Explain it aloud: Roughly how many orders of magnitude slower is reading from flash storage / disk (microseconds to milliseconds) compared to reading from CPU L1 cache (~1 ns) or main RAM (~100 ns)?
- Explain it aloud: How does the OS **Page Cache** speed up repeated file reads and buffer file writes in RAM, and what does calling `fsync()` (or `FileDescriptor.sync()`) force the OS and storage controller to do?
- Explain it aloud: Even on modern mobile flash storage (UFS / eMMC) with no spinning platters, why is sequential I/O still much faster than thousands of tiny random reads and writes?
- Explain it aloud: What does Android's `StrictMode.detectDiskReads()` / `detectDiskWrites()` catch on the Main Thread, and why can even a small SQLite query or `SharedPreferences.commit()` on the UI thread cause dropped frames or ANRs?

## os/android-os/zygote-and-app-processes
problems: none
interview-questions:
- Explain it aloud: Why does Android launch every new app process by calling `fork()` on the **Zygote** process instead of starting a fresh JVM/ART instance from scratch via `exec()`?
- Explain it aloud: How does the Linux kernel's **Copy-on-Write (CoW)** virtual memory mechanism allow 50 running Android apps forked from Zygote to share a single physical RAM copy of core framework classes and resources?
- Explain it aloud: What happens at the hardware page-table level when an app process forked from Zygote modifies a page of memory that was previously marked Copy-on-Write?
- Explain it aloud: What happens during Android boot when `init` starts `Zygote`, and what is the very first special system process that `Zygote` forks (`system_server`)?

## os/android-os/binder-and-system-services
problems: none
interview-questions:
- Explain it aloud: Why did Android introduce the **Binder** kernel driver for IPC instead of using standard Linux pipes or sockets, and how does Binder use `mmap` to transfer data between processes with only a **single copy**?
- Explain it aloud: When your app calls `getSystemService(Context.LOCATION_SERVICE)` or `ActivityManager`, why is your code actually talking to a proxy object that makes a synchronous Binder IPC call to `system_server`?
- Explain it aloud: Why does Android enforce a strict ~1 MB shared limit on the Binder transaction buffer per process, and what causes `TransactionTooLargeException` when passing data in an `Intent` or `Bundle`?
- Explain it aloud: How is Android's **Binder Thread Pool** (up to ~15–16 threads per app process) used when `system_server` or another app invokes an `AIDL` / `ContentProvider` call into your app?

## os/android-os/low-memory-killer
problems: none
interview-questions:
- Explain it aloud: Traditional desktop OSes swap anonymous memory pages out to a disk swap partition when RAM is full—how does Android handle low RAM instead (using `zram` compressed RAM and `lmkd`, the Low Memory Killer Daemon)?
- Explain it aloud: How does Android assign `oom_adj_score` priority buckets (Foreground app, Visible app, Perceptible/Foreground Service, Cached/Background app) so `lmkd` kills the least important process first?
- Explain it aloud: When the user switches to a heavy camera or game app and Android's Low Memory Killer terminates your background app's process, why is `onDestroy()` NOT guaranteed to run before your process dies?
- Explain it aloud: When the user returns to your app after the Low Memory Killer killed its process in the background, how does Android recreate your task stack, and why do unpersisted singletons/static variables reset to `null` while `savedInstanceState` and `SavedStateHandle` survive?

## os/putting-it-together/match-symptoms-to-causes
problems: none
interview-questions:
- Explain it aloud: Symptom: Your app freezes for 5 seconds and shows an **ANR (Application Not Responding)** dialog; the thread dump shows the Main Thread is `BLOCKED` or waiting inside a native Binder call or SQLite lock held by a background thread. Which OS concepts (locks, priority inversion, blocking I/O) explain this, and how do you fix it?
- Explain it aloud: Symptom: Scrolling a `RecyclerView` or `LazyColumn` stutters every few seconds, and Logcat shows frequent `Background concurrent copying GC freed ...` pauses followed by an `OutOfMemoryError`. How do heap allocation rate, GC roots, and memory leaks explain this symptom?
- Explain it aloud: Symptom: A user leaves your checkout screen to copy an OTP from their SMS app, and when they return 10 seconds later, your app restarts on the checkout screen with all form fields blank or crashes with a `NullPointerException` on an in-memory singleton. Which OS event (`lmkd` process death + state restoration) happened?
- Explain it aloud: Symptom: Navigating from Activity A to Activity B with a high-resolution `Bitmap` or a huge list inside `intent.putExtra(...)` crashes immediately with `TransactionTooLargeException`. Which Android OS subsystem (Binder transaction buffer) caused this and where should that data live instead?
- Explain it aloud: Symptom: Your app creates a new thread for every analytics event or image thumbnail, and under heavy load the app crashes with `OutOfMemoryError: pthread_create (1040KB stack) failed: Try again`. Why did `pthread_create` fail even though the Java heap still had free space?

## Self-check
- LeetCode 1279 | Traffic Light Controlled Intersection | Verify exact problem number (1279), Easy difficulty, and that it is a concurrency problem.
- LeetCode 1242 | Web Crawler Multithreaded | Verify exact problem number (1242) and title word order (`Web Crawler Multithreaded`).
- LeetCode 1188 | Design Bounded Blocking Queue | Verify exact problem number (1188) and Medium difficulty.
- LeetCode 1226 | The Dining Philosophers | Verify exact problem number (1226).
- LeetCode 1195 | Fizz Buzz Multithreaded | Verify exact problem number (1195).
- LeetCode 1117 | Building H2O | Verify exact problem number (1117).
- LeetCode 1882 | Process Tasks Using Servers | Verify exact problem number (1882) and Medium difficulty.
- LeetCode 1834 | Single-Threaded CPU | Verify exact problem number (1834) and hyphenation in title.
- LeetCode 582 | Kill Process | Verify exact problem number (582) and Medium difficulty.
- LeetCode 715 | Range Module | Verify exact problem number (715) and Hard difficulty.