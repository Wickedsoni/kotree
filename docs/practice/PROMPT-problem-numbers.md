# Prompt: practice problems for Kotree (copy everything below the line into the other agent)

---

You are helping build a learning website for an Android/Kotlin developer who wants to master Data Structures and Algorithms (dsa), Design and Analysis of Algorithms (daa) and Operating Systems (os) well enough to pass interviews from intern to senior level.

TASK: for EVERY leaf listed below, give a "Practise on these" list of 4 to 8 real problems from LeetCode and HackerRank (optionally GeeksforGeeks or Codeforces, but LeetCode and HackerRank come first) that, done in order, make the reader master exactly that leaf's topic.

RULES (accuracy matters more than volume):
1. Only include a problem if you are SURE of its exact platform number and exact title. LeetCode problems use their problem number (for example 1 Two Sum). HackerRank has no stable numbers: give the exact title, the domain/subdomain path (for example "Algorithms > Sorting") and the URL slug. Never invent a number. If you are not sure, leave it out. A shorter correct list beats a longer wrong one.
2. Order each list easy to hard. Mix: 1 to 2 warm-ups, 2 to 4 core problems, 1 to 2 interview-level stretch problems.
3. For each problem give: platform, number (LeetCode only), exact title, difficulty (Easy/Medium/Hard), URL, and ONE line saying what skill it trains and why it fits this leaf.
4. For the `os` branch, LeetCode has few OS problems. Use the LeetCode Concurrency set (for example 1114 Print in Order, 1115 Print FooBar Alternately, 1116 Print Zero Even Odd, 1117 Building H2O, 1195 Fizz Buzz Multithreaded, 1226 The Dining Philosophers, 1188 Design Bounded Blocking Queue, 1242 Web Crawler Multithreaded, 1279 Traffic Light Controlled Intersection, 1834 Single-Threaded CPU for scheduling), LRU Cache (146) for page replacement and caches, plus HackerRank where it fits. For OS leaves that have no honest practice problem (pure theory), write `problems: none` and give 3 to 5 "Explain it aloud" interview questions instead.
5. Also give, for every leaf, 3 to 5 INTERVIEW QUESTIONS an interviewer actually asks about it (plain questions, no answers).
6. Do not copy problem statements. Titles, numbers, URLs and your one-line note only.
7. Output format: one Markdown file per branch, exactly this shape, so a script can read it. The heading id must match the leaf id given below.

```
# practice: dsa

## dsa/<stem-id>/<leaf-id>
problems:
- LeetCode 217 | Contains Duplicate | Easy | https://leetcode.com/problems/contains-duplicate/ | Hash set membership in O(1)
- HackerRank | Frequency Queries | Medium | https://www.hackerrank.com/challenges/frequency-queries/problem | Two hash maps used together
interview-questions:
- Why is a hash map lookup O(1) on average but O(n) in the worst case?
- ...
```

8. When you finish a branch, add a final section `## Self-check` listing the 10 problem numbers you are LEAST sure about, so a human can verify them.
9. Return three files: practice-dsa.md, practice-daa.md, practice-os.md.

TOPICS (91 leaves). Format: `branch/stem/leaf` : question the leaf answers [difficulty].

## Branch: dsa


### Stem: complexity (Complexity)

- `dsa/complexity/counting-steps` : How many steps does this code take for 10 items, 100 items and 1000 items? [easy]
- `dsa/complexity/big-o-notation` : What does O(n) say about my code, and what does it ignore? [medium]
- `dsa/complexity/reading-complexity-in-code` : Which of these loops hides an O(n²) cost? [medium]
- `dsa/complexity/time-vs-space` : When is it right to spend more memory to save time? [easy]

### Stem: arrays (Arrays)

- `dsa/arrays/arrays-in-memory` : Why is reading item 500 as quick as reading item 1? [easy]
- `dsa/arrays/insert-and-delete-in-an-array` : Why is removing the first item of a big list slow? [easy]
- `dsa/arrays/how-arraylist-grows` : How can a list keep growing when its array has a fixed size? [medium]

### Stem: strings (Strings)

- `dsa/strings/text-building-cost` : Why is joining text with + inside a loop slow? [medium]
- `dsa/strings/compare-text-by-characters` : How do I check two words use the same letters without sorting them? [easy]

### Stem: linked-lists (Linked Lists)

- `dsa/linked-lists/what-is-a-linked-list` : How is a chain of nodes different from a row of boxes? [easy]
- `dsa/linked-lists/insert-and-remove-nodes` : How do I remove a node without breaking the chain? [medium]
- `dsa/linked-lists/linked-list-vs-arraylist` : When is a linked list actually the better choice? [medium]

### Stem: stacks (Stacks)

- `dsa/stacks/what-is-a-stack` : Which item comes out first from a stack? [easy]
- `dsa/stacks/matching-brackets` : How does a stack check that every bracket has its partner? [medium]

### Stem: queues (Queues)

- `dsa/queues/what-is-a-queue` : Who is served first in a queue? [easy]
- `dsa/queues/keeping-the-last-n-items` : How do I keep only the latest 100 log lines? [medium]

### Stem: hashing (Hashing)

- `dsa/hashing/hashing-idea` : How can a lookup take the same time for 10 items or 10 million? [medium]
- `dsa/hashing/collisions` : What happens when two keys land in the same slot? [medium]
- `dsa/hashing/hashcode-and-equals-contract` : Why must equal objects have the same hash code? [hard]

### Stem: recursion (Recursion)

- `dsa/recursion/what-is-recursion` : How can a function call itself and still stop? [medium]
- `dsa/recursion/the-call-stack` : What happens in memory when functions call each other? [hard]
- `dsa/recursion/repeated-work-in-recursion` : Why is the obvious recursive Fibonacci so slow? [medium]

### Stem: sorting (Sorting)

- `dsa/sorting/what-sorting-means` : What does sorting by a key do to items that tie? [easy]
- `dsa/sorting/simple-sorts` : How can swapping neighbours put a list in order? [easy]
- `dsa/sorting/merge-sort` : How does splitting a list in half and merging make sorting fast? [medium]
- `dsa/sorting/quick-sort` : How does picking a pivot sort a list? [hard]
- `dsa/sorting/sorting-in-the-standard-library` : What does sortedBy actually do for me, and what does it cost? [easy]

### Stem: searching (Searching)

- `dsa/searching/binary-search` : How can I find a name in a sorted list in about 20 steps? [medium]
- `dsa/searching/binary-search-boundaries` : Why do so many binary search versions have an off-by-one bug? [hard]

### Stem: trees (Trees)

- `dsa/trees/what-is-a-tree` : Why do folders, menus and screens all form a tree? [easy]
- `dsa/trees/walking-a-tree` : In what order can I visit every node of a tree? [medium]
- `dsa/trees/level-order-walk` : How do I visit a tree one level at a time? [medium]

### Stem: bst (Binary Search Trees)

- `dsa/bst/bst-search-and-insert` : Why is searching a sorted tree as fast as binary search? [medium]
- `dsa/bst/bst-shape-and-balance` : Why does adding already-sorted data make a search tree slow? [medium]

### Stem: heaps (Heaps and Priority Queues)

- `dsa/heaps/priority-queue-idea` : How does the most urgent task always come out first? [easy]
- `dsa/heaps/how-a-heap-works` : How is a heap stored in a plain array? [medium]

### Stem: graphs (Graphs)

- `dsa/graphs/what-is-a-graph` : What is a graph? [easy]
- `dsa/graphs/storing-a-graph` : How do I store a graph in code? [easy]
- `dsa/graphs/breadth-first-search` : How do I find the fewest hops between two nodes? [medium]
- `dsa/graphs/depth-first-search` : How do I explore every path to the end before turning back? [medium]
- `dsa/graphs/dependency-order` : In what order should steps with dependencies run? [hard]

### Stem: tries (Tries)

- `dsa/tries/what-is-a-trie` : How does a search box suggest words after only two letters? [medium]

### Stem: patterns (Everyday Patterns)

- `dsa/patterns/two-pointers` : How can two markers walking a list replace a nested loop? [medium]
- `dsa/patterns/sliding-window` : How do I find the best stretch of a list in one pass? [medium]
- `dsa/patterns/prefix-sums` : How can I answer many range-total questions instantly? [easy]
- `dsa/patterns/choose-the-right-structure` : Which structure would I reach for, and what will it cost? [hard]

## Branch: daa


### Stem: analysis (Analysis of Algorithms)

- `daa/analysis/best-worst-average-case` : Why can the same code be fast on one input and slow on another? [easy]
- `daa/analysis/big-o-omega-theta` : What do O, Omega and Theta each promise? [medium]
- `daa/analysis/loop-invariants` : How can I be sure a loop gives the right answer for every input, not just my tests? [medium]

### Stem: recurrences (Recurrences)

- `daa/recurrences/writing-a-recurrence` : How do I turn a recursive function into an equation for its cost? [medium]
- `daa/recurrences/solving-recurrences` : How do I get the cost out of T(n) = 2T(n/2) + n without guessing? [hard]

### Stem: divide-and-conquer (Divide and Conquer)

- `daa/divide-and-conquer/split-solve-combine` : When does splitting a problem into smaller copies of itself pay off? [easy]
- `daa/divide-and-conquer/merge-sort-cost` : Why does merge sort take about n log n steps? [medium]
- `daa/divide-and-conquer/find-the-median-without-sorting` : How can I find the middle value without sorting everything? [hard]

### Stem: greedy (Greedy Algorithms)

- `daa/greedy/greedy-choice-idea` : When can I take the best-looking choice at each step and still get the best total? [easy]
- `daa/greedy/interval-scheduling` : How many meetings can fit in one room? [medium]
- `daa/greedy/when-greedy-fails` : Why does taking the biggest coin first sometimes use too many coins? [medium]

### Stem: dynamic-programming (Dynamic Programming)

- `daa/dynamic-programming/overlapping-subproblems` : How do I spot a problem that keeps solving the same smaller problem? [easy]
- `daa/dynamic-programming/memoization` : How does remembering answers turn a slow function into a fast one? [medium]
- `daa/dynamic-programming/tabulation` : How do I fill a table from the smallest case upward so no recursion is needed? [medium]
- `daa/dynamic-programming/pick-items-within-a-limit` : How do I choose which items to keep when space or time is limited? [hard]
- `daa/dynamic-programming/compare-two-texts` : How different are two pieces of text, and which edits turn one into the other? [hard]

### Stem: backtracking (Backtracking)

- `daa/backtracking/backtracking-idea` : How do I try every possibility but undo a step when it leads nowhere? [easy]
- `daa/backtracking/pruning-the-search` : How do I stop exploring paths that can no longer win? [medium]

### Stem: graph-algorithms (Graph Algorithms)

- `daa/graph-algorithms/shortest-path-dijkstra` : How does a map app find the fastest route? [medium]
- `daa/graph-algorithms/shortest-path-with-a-heuristic` : How can a guess about the distance left make the search faster? [medium]

### Stem: hard-problems (Hard Problems)

- `daa/hard-problems/p-np-in-plain-words` : Why can't an app promise the best route through 30 stops? [medium]

### Stem: choosing-an-approach (Choosing an Approach)

- `daa/choosing-an-approach/choose-a-strategy` : Is this problem split-and-combine, greedy, remembered subproblems, backtracking or a known hard one? [hard]

## Branch: os


### Stem: what-an-os-does (What an Operating System Does)

- `os/what-an-os-does/what-the-os-does-for-apps` : What does the operating system do for my app that my code cannot do alone? [easy]
- `os/what-an-os-does/kernel-and-system-calls` : How does my app ask the operating system to use the hardware for it? [easy]

### Stem: processes (Processes)

- `os/processes/what-is-a-process` : Why does one crashing app leave all the other apps running? [easy]
- `os/processes/process-states` : What is a process doing while it waits for its turn? [medium]
- `os/processes/passing-data-between-processes` : How can two apps share data if neither can see the other's memory? [medium]

### Stem: memory (Memory)

- `os/memory/stack-and-heap` : Why do local variables disappear when a function ends but objects can live on? [medium]
- `os/memory/what-keeps-an-object-alive` : Why is an object I no longer need still using memory? [medium]
- `os/memory/virtual-memory` : Why does every app think it has the whole memory to itself? [hard]

### Stem: threads (Threads)

- `os/threads/what-is-a-thread` : How is a thread different from a process? [medium]
- `os/threads/what-a-thread-costs` : Why not start a new thread for every small job? [easy]
- `os/threads/a-thread-that-waits` : What happens to a thread while it waits for a file or a reply? [medium]

### Stem: scheduling (Scheduling)

- `os/scheduling/one-cpu-many-apps` : How can one CPU core run ten apps at the same time? [easy]
- `os/scheduling/priorities-and-starvation` : Why can a low-priority job wait forever? [easy]

### Stem: synchronisation (Synchronisation)

- `os/synchronisation/race-conditions` : Why can two threads updating one counter lose an update? [hard]
- `os/synchronisation/locks-and-critical-sections` : How does a lock let only one thread into a risky piece of code? [medium]
- `os/synchronisation/producer-and-consumer` : How can one thread hand work to another without wasting CPU? [medium]

### Stem: deadlock (Deadlock)

- `os/deadlock/what-is-a-deadlock` : How can two threads wait for each other forever? [hard]

### Stem: files (Files)

- `os/files/files-folders-and-owners` : Why can my app open some folders but not others? [easy]
- `os/files/why-disk-is-slow` : Why is reading a file so much slower than reading a variable? [easy]

### Stem: android-os (Android Under the App)

- `os/android-os/zygote-and-app-processes` : How does Android create an app process so quickly? [medium]
- `os/android-os/binder-and-system-services` : How does my app call a system service that lives in another process? [medium]
- `os/android-os/low-memory-killer` : Why did Android close my app while I was away? [medium]

### Stem: putting-it-together (Putting It Together)

- `os/putting-it-together/match-symptoms-to-causes` : Which operating-system event explains this app symptom? [hard]
