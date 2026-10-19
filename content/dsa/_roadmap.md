```yaml
branch: dsa
bough: cs-theory
stems:
  - id: complexity
    title: Complexity
    why: A fresher needs to count steps before choosing between two approaches, because that is how a slow list or a frozen screen is explained.
    leaves:
      - id: counting-steps
        title: How many steps does this code take for 10 items, 100 items and 1000 items?
        difficulty: easy
        requires: []
        fruit: false
      - id: big-o-notation
        title: What does O(n) say about my code, and what does it ignore?
        difficulty: medium
        requires: [dsa/complexity/counting-steps]
        fruit: false
      - id: reading-complexity-in-code
        title: Which of these loops hides an O(n²) cost?
        difficulty: medium
        requires: [dsa/complexity/big-o-notation]
        fruit: false
      - id: time-vs-space
        title: When is it right to spend more memory to save time?
        difficulty: easy
        requires: [dsa/complexity/big-o-notation]
        fruit: false
  - id: arrays
    title: Arrays
    why: Lists on every screen sit on arrays, so a fresher must know why reading is instant but inserting is slow.
    leaves:
      - id: arrays-in-memory
        title: Why is reading item 500 as quick as reading item 1?
        difficulty: easy
        requires: [dsa/complexity/big-o-notation]
        fruit: false
      - id: insert-and-delete-in-an-array
        title: Why is removing the first item of a big list slow?
        difficulty: easy
        requires: [dsa/arrays/arrays-in-memory]
        fruit: false
      - id: how-arraylist-grows
        title: How can a list keep growing when its array has a fixed size?
        difficulty: medium
        requires: [dsa/arrays/insert-and-delete-in-an-array]
        fruit: false
  - id: strings
    title: Strings
    why: Text is the most common data in an app, and careless text handling is a quiet source of slowness.
    leaves:
      - id: text-building-cost
        title: Why is joining text with + inside a loop slow?
        difficulty: medium
        requires: [dsa/arrays/arrays-in-memory, dsa/complexity/reading-complexity-in-code]
        fruit: false
      - id: compare-text-by-characters
        title: How do I check two words use the same letters without sorting them?
        difficulty: easy
        requires: [dsa/arrays/arrays-in-memory]
        fruit: false
  - id: linked-lists
    title: Linked Lists
    why: A chain of nodes shows what happens when items are tied by references instead of sitting side by side.
    leaves:
      - id: what-is-a-linked-list
        title: How is a chain of nodes different from a row of boxes?
        difficulty: easy
        requires: [dsa/arrays/insert-and-delete-in-an-array]
        fruit: false
      - id: insert-and-remove-nodes
        title: How do I remove a node without breaking the chain?
        difficulty: medium
        requires: [dsa/linked-lists/what-is-a-linked-list]
        fruit: false
      - id: linked-list-vs-arraylist
        title: When is a linked list actually the better choice?
        difficulty: medium
        requires: [dsa/linked-lists/insert-and-remove-nodes, dsa/arrays/how-arraylist-grows]
        fruit: false
  - id: stacks
    title: Stacks
    why: Back buttons, undo and nested tags all work like a pile where the last item in is the first out.
    leaves:
      - id: what-is-a-stack
        title: Which item comes out first from a stack?
        difficulty: easy
        requires: [dsa/arrays/arrays-in-memory]
        fruit: false
      - id: matching-brackets
        title: How does a stack check that every bracket has its partner?
        difficulty: medium
        requires: [dsa/stacks/what-is-a-stack]
        fruit: false
  - id: queues
    title: Queues
    why: Anything that must be handled in arrival order, such as tasks and messages, waits in a queue.
    leaves:
      - id: what-is-a-queue
        title: Who is served first in a queue?
        difficulty: easy
        requires: [dsa/stacks/what-is-a-stack]
        fruit: false
      - id: keeping-the-last-n-items
        title: How do I keep only the latest 100 log lines?
        difficulty: medium
        requires: [dsa/queues/what-is-a-queue]
        fruit: false
  - id: hashing
    title: Hashing
    why: Maps and sets make lookups instant, and misusing them is the quickest way to write a slow or wrong lookup.
    leaves:
      - id: hashing-idea
        title: How can a lookup take the same time for 10 items or 10 million?
        difficulty: medium
        requires: [dsa/complexity/reading-complexity-in-code, dsa/arrays/arrays-in-memory]
        fruit: false
      - id: collisions
        title: What happens when two keys land in the same slot?
        difficulty: medium
        requires: [dsa/hashing/hashing-idea, dsa/linked-lists/what-is-a-linked-list]
        fruit: false
      - id: hashcode-and-equals-contract
        title: Why must equal objects have the same hash code?
        difficulty: hard
        requires: [dsa/hashing/collisions]
        fruit: false
  - id: recursion
    title: Recursion
    why: Trees, folders and nested screens are naturally recursive, and a wrong stopping rule crashes an app.
    leaves:
      - id: what-is-recursion
        title: How can a function call itself and still stop?
        difficulty: medium
        requires: []
        fruit: false
      - id: the-call-stack
        title: What happens in memory when functions call each other?
        difficulty: hard
        requires: [dsa/recursion/what-is-recursion, dsa/stacks/what-is-a-stack]
        fruit: false
      - id: repeated-work-in-recursion
        title: Why is the obvious recursive Fibonacci so slow?
        difficulty: medium
        requires: [dsa/recursion/what-is-recursion, dsa/complexity/big-o-notation]
        fruit: true
  - id: sorting
    title: Sorting
    why: Lists of contacts, messages and prices are sorted all day, and the cost of sorting grows fast with size.
    leaves:
      - id: what-sorting-means
        title: What does sorting by a key do to items that tie?
        difficulty: easy
        requires: [dsa/arrays/arrays-in-memory]
        fruit: false
      - id: simple-sorts
        title: How can swapping neighbours put a list in order?
        difficulty: easy
        requires: [dsa/sorting/what-sorting-means, dsa/complexity/reading-complexity-in-code]
        fruit: false
      - id: merge-sort
        title: How does splitting a list in half and merging make sorting fast?
        difficulty: medium
        requires: [dsa/sorting/simple-sorts, dsa/recursion/what-is-recursion]
        fruit: false
      - id: quick-sort
        title: How does picking a pivot sort a list?
        difficulty: hard
        requires: [dsa/sorting/merge-sort]
        fruit: false
      - id: sorting-in-the-standard-library
        title: What does sortedBy actually do for me, and what does it cost?
        difficulty: easy
        requires: [dsa/sorting/quick-sort, dsa/sorting/simple-sorts]
        fruit: false
  - id: searching
    title: Searching
    why: Finding one item among many is the most common question an app asks of its data.
    leaves:
      - id: binary-search
        title: How can I find a name in a sorted list in about 20 steps?
        difficulty: medium
        requires: [dsa/sorting/what-sorting-means, dsa/complexity/big-o-notation]
        fruit: false
      - id: binary-search-boundaries
        title: Why do so many binary search versions have an off-by-one bug?
        difficulty: hard
        requires: [dsa/searching/binary-search]
        fruit: false
  - id: trees
    title: Trees
    why: Folders, screens and menus are trees, and a fresher must be able to walk one without getting lost.
    leaves:
      - id: what-is-a-tree
        title: Why do folders, menus and screens all form a tree?
        difficulty: easy
        requires: [dsa/recursion/what-is-recursion, dsa/linked-lists/what-is-a-linked-list]
        fruit: false
      - id: walking-a-tree
        title: In what order can I visit every node of a tree?
        difficulty: medium
        requires: [dsa/trees/what-is-a-tree, dsa/recursion/the-call-stack]
        fruit: false
      - id: level-order-walk
        title: How do I visit a tree one level at a time?
        difficulty: medium
        requires: [dsa/trees/walking-a-tree, dsa/queues/what-is-a-queue]
        fruit: false
  - id: bst
    title: Binary Search Trees
    why: A sorted tree shows how search stays fast while data keeps changing, and what breaks it.
    leaves:
      - id: bst-search-and-insert
        title: Why is searching a sorted tree as fast as binary search?
        difficulty: medium
        requires: [dsa/searching/binary-search, dsa/trees/walking-a-tree]
        fruit: false
      - id: bst-shape-and-balance
        title: Why does adding already-sorted data make a search tree slow?
        difficulty: medium
        requires: [dsa/bst/bst-search-and-insert]
        fruit: false
  - id: heaps
    title: Heaps and Priority Queues
    why: When the most urgent item must always come out first, a heap does it without sorting everything.
    leaves:
      - id: priority-queue-idea
        title: How does the most urgent task always come out first?
        difficulty: easy
        requires: [dsa/queues/what-is-a-queue, dsa/sorting/what-sorting-means]
        fruit: false
      - id: how-a-heap-works
        title: How is a heap stored in a plain array?
        difficulty: medium
        requires: [dsa/heaps/priority-queue-idea, dsa/trees/what-is-a-tree, dsa/arrays/arrays-in-memory]
        fruit: false
  - id: graphs
    title: Graphs
    why: Maps, social links, screen flows and build steps are graphs, and a fresher needs to visit them without looping forever.
    leaves:
      - id: what-is-a-graph
        title: What is a graph?
        difficulty: easy
        requires: [dsa/trees/what-is-a-tree]
        fruit: false
      - id: storing-a-graph
        title: How do I store a graph in code?
        difficulty: easy
        requires: [dsa/graphs/what-is-a-graph, dsa/hashing/hashing-idea]
        fruit: false
      - id: breadth-first-search
        title: How do I find the fewest hops between two nodes?
        difficulty: medium
        requires: [dsa/graphs/storing-a-graph, dsa/trees/level-order-walk]
        fruit: false
      - id: depth-first-search
        title: How do I explore every path to the end before turning back?
        difficulty: medium
        requires: [dsa/graphs/storing-a-graph, dsa/trees/walking-a-tree]
        fruit: false
      - id: dependency-order
        title: In what order should steps with dependencies run?
        difficulty: hard
        requires: [dsa/graphs/depth-first-search]
        fruit: false
  - id: tries
    title: Tries
    why: Search-as-you-type needs prefixes found fast, and a trie shows how to share the start of many words.
    leaves:
      - id: what-is-a-trie
        title: How does a search box suggest words after only two letters?
        difficulty: medium
        requires: [dsa/trees/what-is-a-tree, dsa/hashing/hashing-idea]
        fruit: false
  - id: patterns
    title: Everyday Patterns
    why: A few small tricks turn slow nested loops into one pass, and the last lesson ties every structure to a choice.
    leaves:
      - id: two-pointers
        title: How can two markers walking a list replace a nested loop?
        difficulty: medium
        requires: [dsa/arrays/arrays-in-memory, dsa/sorting/what-sorting-means]
        fruit: false
      - id: sliding-window
        title: How do I find the best stretch of a list in one pass?
        difficulty: medium
        requires: [dsa/patterns/two-pointers]
        fruit: false
      - id: prefix-sums
        title: How can I answer many range-total questions instantly?
        difficulty: easy
        requires: [dsa/arrays/arrays-in-memory]
        fruit: false
      - id: choose-the-right-structure
        title: Which structure would I reach for, and what will it cost?
        difficulty: hard
        requires: [dsa/patterns/prefix-sums, dsa/heaps/how-a-heap-works, dsa/graphs/depth-first-search, dsa/tries/what-is-a-trie]
        fruit: true
```

## Branch prerequisites

- Assumes Foundation: `foundation/programming-thinking/what-is-a-list` — arrays and every later structure build on the idea of numbered boxes.
- Assumes Foundation: `foundation/programming-thinking/counting-loops` — step counting starts from a loop with a known number of turns.
- Assumes Foundation: `foundation/programming-thinking/trace-code-by-hand` — every sim and challenge asks the learner to follow code with a pencil.
- Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list` — the branch uses `List`, `MutableList` and `ArrayDeque` without re-explaining them.
- Assumes Kotlin language: `kotlin/classes-and-objects/objects-are-references` — linked nodes, trees and graphs are objects pointing at other objects.
- Assumes Kotlin language: `kotlin/lambdas/map-filter-and-friends` — sorting and searching examples use `sortedBy` and `filter`.
- Helps to know Android core and lifecycles: `android-core/activities/back-stack` — the stack leaf uses the back button as its everyday example; it is recapped in one paragraph.
- Helps to know UI (Compose and Views): `ui/lists/list-keys` — the hashing leaves mention list keys.
- Helps to know Operating systems: `os/memory/stack-and-heap` — the call-stack and queue leaves recap what a thread's stack is in one paragraph.

Foundation is recommended first ("Start here"), then `kotlin`. Phase plan: complexity, arrays and strings open in phase 2; linked lists to recursion in phase 3; sorting to binary search trees in phase 4; heaps to patterns in phase 5.

Every theory leaf names where the idea shows up in a real app in its Notes cell (`Android:`), as the Theory-Scared Student requires (`docs/01` §2.3). Android lines describe what a user can see; anything about library internals is marked `⚠️ VERIFY` against developer.android.com before the leaf goes live. No complexity number is attached to an Android class in this roadmap. All examples are plain Kotlin that runs in the browser editor.

All 46 leaves are `shadow` on 18 Oct 2026. Nothing is published.

## Stems

### complexity · Complexity

Flags: phase=2 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `counting-steps` | easy | "If a loop takes 10 steps for 10 items, how many steps does it take for 100 and for 1000?" | A postman counting footsteps: twice as many houses means twice as many steps. | "I can judge speed by running the code once on my laptop." | `sim:` a row of balls with a step counter; a slider sets 10, 100 or 1000 items and a bar grows next to a second bar for a nested loop. | `P:` how many times `println` runs for n = 5 with one loop and with two nested loops · `F(ai):` a tidy "find the largest" that sorts the whole list first · `B:` double n and watch both bars | Android: a list screen that feels fine with 20 test items and stutters with 5,000 real ones. Assumes Foundation: `foundation/programming-thinking/counting-loops`; Assumes Kotlin language: `kotlin/control-flow/ranges-and-for-loops`. This is the `docs/01` §6.2 check ("count steps for 10, 100 and 1000 items"). |
| `big-o-notation` | medium | "What does O(n) tell me about my code, and what does it leave out?" | A taxi quote of "about 2 per km": the rate matters for long trips, the small starting fee does not. | "O(n) means exactly n steps." | `sim:` reuse `dsa/complexity/counting-steps` sim, with a "drop the small parts" switch that turns 3n + 7 into n and overlays the two curves. | `P:` which grows faster at n = 1000, n² or 50n · `P:` simplify 3n² + 5n + 2 · `B:` set n to 3 and see the "slower" shape win | Android: why a screen that re-sorts its list on every redraw falls behind as the list grows. Covers O(1), O(log n), O(n), O(n log n), O(n²) as a names-and-pictures table; formal O, Omega and Theta wait for `daa/analysis/big-o-omega-theta`. |
| `reading-complexity-in-code` | medium | "How do I spot a hidden second loop inside one innocent line?" | A school corridor where every student shakes hands with every other student: n students, about n × n handshakes. | "One loop in my code means O(n)." | `sim:` a code panel with a heat strip; the learner taps a line and sees how many hidden steps one call costs (`contains`, `indexOf`, `remove`); balls pile up when it sits inside a loop. | `F(ai):` a neat "remove duplicates" that calls `contains` on a list inside a loop · `F(ai):` a "merge server items with local items" function that scans the local list for every server item · `P:` which of three versions is fastest for 10,000 items | Android: a refresh of a big feed that janks because items are matched with a scan of the whole list for each one. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |
| `time-vs-space` | easy | "When is it fine to use more memory to save time?" | Keeping a printed bus timetable in your pocket: it takes pocket space, but you skip the trip to the station every time. | "Using less memory is always better." | `sim:` a seesaw: dropping a "remember answers" weight on the memory side lifts the time side; an unbounded pile eventually tips the whole seesaw. | `P:` how many calls run with and without remembered answers · `F(ai):` a generated cache `Map` that grows forever · `B:` cap the cache at 100 entries | Android: an in-memory image cache gives smoother scrolling but costs memory the system may reclaim by closing apps. ⚠️ VERIFY wording on memory pressure and `LruCache` at developer.android.com before the leaf goes live. |

### arrays · Arrays

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `arrays-in-memory` | easy | "Why does asking for item 500 take as long as asking for item 1?" | A row of numbered lockers: the number tells you exactly where to walk, so you never count past the others. | "To reach item 500 the computer must step over the first 499." | `sim:` a row of lockers; the learner drags a hand to any number and one tick is counted; a second row of bead-and-string items needs one tick per bead walked. | `P:` the value of `arr[2]` after three assignments · `F:` a loop that reads `arr[arr.size]` · `B:` ask for index -1 | Android: a scrolling list asks its data for "the item at position p" for each row it draws; a bitmap's pixels are also one huge numbered row. Assumes Foundation: `foundation/programming-thinking/what-is-a-list`; Assumes Kotlin language: `kotlin/collections/arrays-vs-lists`. |
| `insert-and-delete-in-an-array` | easy | "Why is removing the first item of a big list slow but removing the last one fast?" | A cinema row: if someone takes the first seat, everyone else shuffles along one seat. | "Removing an item just leaves a gap and costs nothing." | `sim:` a tray of balls; removing the first ball makes every other ball slide left one tick at a time; removing the last ball costs one tick. | `P:` the list after `removeAt(0)` on four items · `F(ai):` a task queue built with `removeAt(0)` in a loop over 100,000 items · `B:` remove from the end instead | Android: a chat screen that drops the oldest message from the top of a big list on every new message. ⚠️ VERIFY: behaviour of `removeFirst()` and `removeLast()` on current Android and JDK versions (kotlinlang.org) before any example uses them. |
| `how-arraylist-grows` | medium | "How can a list keep growing when its underlying array has a fixed size?" | Moving to a bigger flat each time the furniture no longer fits: each move is costly, but moves are rare, so the average cost per year stays small. | "Adding to a list costs nothing, whatever its size." | `sim:` a tray with 4 slots; when it is full a double-size tray appears and the balls copy across; a counter shows total copies after n adds. | `P:` how many copies happen for 8 adds when the size doubles each time · `F(ai):` `result = result + item` inside a loop, which builds a new list every turn · `B:` create the list with a starting capacity | Android: building a big list of rows in a loop before showing it, and why `list += item` in a loop is a trap. The formal "amortised" argument is kept in `daa` `## Later`; here it is only the picture. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |

### strings · Strings

Flags: phase=2

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `text-building-cost` | medium | "Why is building a long text with `+=` in a loop slow, and what do I use instead?" | Rewriting a whole letter from the top every time you add one more word. | "A String grows in place when I add text to it." | `sim:` a belt of letter tiles; each `+` copies the whole belt into a new one while a builder box just adds a tile; a counter shows tiles moved. | `P:` how many tiles are copied to join 5 words one at a time · `F(ai):` a tidy CSV export that uses `+=` for 50,000 rows · `B:` swap in `buildString` | Android: exporting a long log or CSV while the screen waits. Assumes Kotlin language: `kotlin/strings/everyday-string-functions` (Strings never change). ⚠️ VERIFY: what the current Kotlin compiler does with `+=` in loops (kotlinlang.org). |
| `compare-text-by-characters` | easy | "How can I tell that "listen" and "silent" use the same letters without sorting them?" | Two bags of Scrabble tiles: the words match if both bags hold the same tiles. | "To compare the letters of two words I must sort them or compare every pair." | `sim:` 26 bins; letters of word one drop in (+1) and letters of word two lift out (-1); all bins at zero means a match. | `P:` the bin counts after "listen" goes in and "silent" comes out · `F:` upper and lower case counted as different letters · `B:` add a space to one word | Android: username and password rules ("no repeated characters"), and the normalising step of a search box. Assumes Kotlin language: `kotlin/strings/strings-are-sequences`. |

### linked-lists · Linked Lists

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-linked-list` | easy | "How is a chain of nodes different from a row of boxes?" | A treasure hunt: each clue only tells you where the next clue is hidden. | "The items of a linked list sit side by side in memory, like an array." | `sim:` beads scattered on a table, each tied to the next by a string; the learner drags a bead and the chain stretches; finding the 5th bead means walking the strings one by one. | `P:` the value reached after following `next` three times · `F:` the last node's `next` was never set to null, so the walk never ends · `B:` cut one string | Android: a playlist where every song points to the next; the main thread's waiting messages are also described as a chain. ⚠️ VERIFY against the `Looper`/`MessageQueue` pages on developer.android.com before saying so. Assumes Kotlin language: `kotlin/classes-and-objects/objects-are-references`, `kotlin/null-safety/nullable-types`. |
| `insert-and-remove-nodes` | medium | "In what order do I change the links so the chain does not break?" | A conga line: to let someone in, the person in front lets go and grabs the newcomer, who grabs the next person. | "Removing a node deletes it and the chain mends itself." | `sim:` reuse `dsa/linked-lists/what-is-a-linked-list` sim, with grabbable strings; letting go of the rest of the chain first drops the tail on the floor. | `P:` the list after removing the second node · `F:` an insert that sets links in the wrong order and loses the tail · `B:` remove the last node | Android: moving one item in the middle of a to-do list or playlist without sliding every other item. Assumes Kotlin language: `kotlin/classes-and-objects/what-is-a-class`. |
| `linked-list-vs-arraylist` | medium | "Why is an ArrayList usually the better choice even though a linked list inserts without sliding?" | A pocket address book where entries must slide to make room, versus loose index cards tied with string, where you must follow the string to find a card. | "Linked lists are always faster at insert and delete." | `sim:` a race with two lanes; the learner picks list size and operation (front insert, middle insert, read by position) and ticks are counted in each lane. | `P:` which structure takes more steps for `get(5000)` · `F(ai):` generated code that uses `LinkedList` and calls `get(i)` in a loop · `B:` switch it to `ArrayList` | Android: screen data is almost always held in array-backed lists; linked structures live inside libraries (for example a cache that tracks which items were used last). ⚠️ VERIFY that `LruCache` is built on `LinkedHashMap` at developer.android.com. |

### stacks · Stacks

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-stack` | easy | "Which item comes out first from a stack, and why can I never take one from the middle?" | A pile of canteen plates: you add on top and take from the top. | "A stack lets me take any item I like." | `sim:` real plates dropped one by one into a pile; pulling a plate from the middle topples the pile, pulling the top one does not. | `P:` the order items come out after pushing 1, 2, 3 · `F:` a "pop" written with `removeAt(0)` · `B:` pop from an empty stack | Android: the Back button; screens pile up and Back removes the top one. Helps to know Android core and lifecycles: `android-core/activities/back-stack`. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. ⚠️ VERIFY: which `ArrayDeque` functions to show given the `removeLast` note in `dsa/arrays/insert-and-delete-in-an-array`. |
| `matching-brackets` | medium | "Why does a stack catch a `(]` that simple counting misses?" | Nested boxes: the last box you opened must be the first one you close. | "Counting opening and closing brackets is enough." | `sim:` reuse `dsa/stacks/what-is-a-stack` sim, with plates shaped like open brackets; a closing bracket must fit the top plate or the line jams. | `P:` the verdict for `"([)]"` · `F(ai):` a tidy bracket checker that only counts · `B:` add one extra closing bracket | Android: reading a layout file means closing every tag in the reverse order it was opened; undo and redo in an editor use two piles. Helps to know UI (Compose and Views): `ui/views-and-interop/reading-xml-layouts`. Assumes Kotlin language: `kotlin/control-flow/when-expression`, `kotlin/strings/strings-are-sequences`. |

### queues · Queues

Flags: phase=3

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-queue` | easy | "In what order do items come out of a queue?" | People waiting at a ticket counter: the one who arrived first is served first. | "A queue and a stack are the same thing with different names." | `sim:` a counter with people-balls on one side and the plate pile of `dsa/stacks/what-is-a-stack` on the other; the learner sends the same five items through both and compares the order. | `P:` the order out after A, B, C join · `F(ai):` a task runner that uses `removeLast` so the newest task goes first · `B:` serve from an empty queue | Android: the main thread handles taps and drawing one message after another; background jobs and downloads wait in line. ⚠️ VERIFY wording about the main thread's message queue on developer.android.com. Helps to know Android core and lifecycles: `android-core/background-work/what-is-workmanager`. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |
| `keeping-the-last-n-items` | medium | "How do I keep only the latest 100 log lines without copying the whole list each time?" | A rolling whiteboard of the last five lap times: when it is full you rub out the oldest and write the newest. | "To keep the last N I must copy the list and cut it every time." | `sim:` a wheel with N slots that turns; a new ball pushes the oldest ball out; the learner changes N and the speed. | `P:` the contents after adding 1 to 7 into a buffer of 5 · `F:` a buffer that never drops the oldest, so memory grows · `B:` set N to 1 | Android: the "last 10 searches" under a search bar; the system log also keeps only recent lines in a fixed-size area. ⚠️ VERIFY wording on logcat buffers at developer.android.com. |

### hashing · Hashing

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `hashing-idea` | medium | "Why does a `Set` answer 'is this inside?' in about one step for 10 items or 10 million?" | A cloakroom: your ticket number tells the attendant which hook to walk to, with no searching. | "A map checks every key until it finds mine, just faster." | `sim:` a wall of hooks; a chute machine turns each ticket into a hook number and drops the coat there; a list of the same coats needs a walk past every hook. | `P:` which finishes first, 100,000 `list.contains` calls or 100,000 `set.contains` calls · `F(ai):` a de-duplicating loop that calls `list.contains` · `B:` give every coat the same ticket number | Android: key-value storage such as saved settings looks values up by key, and Compose list keys identify rows the same way. Helps to know UI (Compose and Views): `ui/lists/list-keys`. Assumes Kotlin language: `kotlin/collections/sets`, `kotlin/collections/maps`. |
| `collisions` | medium | "What happens when two keys land on the same hook, and why does it slow lookups?" | Two coats whose tickets point to the same hook: they hang together and the attendant checks both. | "A hash function gives every key its own private slot." | `sim:` reuse `dsa/hashing/hashing-idea` sim, with a slider that shrinks the number of hooks; coats pile on one hook and lookups slow; "add hooks" spreads them out again. | `P:` the hook for each word when hook = word length % 5 · `F:` a "hash" that returns only the first letter, so most keys clump · `B:` set the hooks to 1 | Android: any map-based lookup slows down when keys hash badly, for example a model class whose `hashCode` returns a constant. Assumes Kotlin language: `kotlin/collections/maps`. |
| `hashcode-and-equals-contract` | hard | "Why can a `Map` lose an object I just put in it?" | Two identical coats must always go on the same hook, or the attendant will never find the second one. | "Overriding `equals` alone is enough." | `sim:` reuse `dsa/hashing/hashing-idea` sim, with two look-alike coats given different hook numbers so the lookup fails; fixing the rule makes them meet. | `P:` the result of `set.contains` for a class with `equals` but no `hashCode` · `F(ai):` a generated class that overrides `equals` only and is used as a map key · `F(ai):` a `data class` with a `var` field changed after it was put in a `Set` · `B:` mutate the key and look it up | Android: "item not found" bugs when models sit in a `Set` or `Map` inside a ViewModel or list. Assumes Kotlin language: `kotlin/special-classes/data-classes`, `kotlin/classes-and-objects/two-kinds-of-equality`. |

### recursion · Recursion

Flags: phase=3 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-recursion` | medium | "Why does a function that calls itself eventually stop?" | Russian nesting dolls: open each one until you reach the smallest solid doll, which is the stopping rule. | "A recursive function runs forever unless I escape with a loop." | `sim:` nested dolls that open one inside another; removing the solid smallest doll (the base case) makes the dolls fall forever. | `P:` the output of `countdown(3)` · `F(ai):` a tidy `factorial` whose base case is `n == 1` and crashes for 0 · `F(ai):` a list sum with no rule for the empty list · `B:` remove the base case | Android: a storage screen that adds up a folder's size by adding up the folders inside it. Assumes Foundation: `foundation/programming-thinking/what-is-a-function`; Assumes Kotlin language: `kotlin/functions/declaring-functions`. |
| `the-call-stack` | hard | "What does the computer remember while a function waits for another function to finish?" | A tray stack in a kitchen: each unfinished order goes on top and you finish the top one first. | "Each call forgets the one before it, so recursion uses no extra memory." | `sim:` reuse `dsa/stacks/what-is-a-stack` sim, with a plate for each call holding its own `n`; the pile has a ceiling and touching it ends in a stack-overflow crash. | `P:` the print order when a function prints before and after its recursive call · `F(ai):` a recursion whose input never reaches the base case · `P:` which input depth crashes · `B:` call it with 100,000 | Android: a stack-overflow crash in a bug report caused by a function that keeps calling itself. Helps to know Operating systems: `os/memory/stack-and-heap`; a one-paragraph recap of a thread's stack. Assumes Kotlin language: `kotlin/functions/declaring-functions`. |
| `repeated-work-in-recursion` | medium | "Why is the obvious recursive Fibonacci so slow for n = 45?" | Phoning the same relative for the same phone number again and again instead of writing it on a sticky note. | "Recursive code runs as fast as the loop version because it does the same thing." | `sim:` the call tree of `fib(n)` as a branching shower of balls; duplicate calls glow red; a "sticky note shelf" makes the red balls vanish. | `P:` the number of calls for `fib(5)` · `F(ai):` a neat recursive `fib` that hangs for n = 45 · `B:` add a note shelf (a `Map`) | Android: a recursive total or layout calculation that quietly takes seconds on real data and freezes the screen. The idea returns as `daa/dynamic-programming/memoization`. Fruit (end of the phase-3 segment). Assumes Kotlin language: `kotlin/collections/maps`. |

### sorting · Sorting

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-sorting-means` | easy | "What order do two equal items end up in after a sort?" | Books on a shelf arranged by height: two books of the same height stay in the order you found them. | "Equal items come out in a random order." | `sim:` coloured balls of different heights on a shelf; the learner sorts by height and a toggle shows whether equal heights keep their colour order (stable) or swap. | `P:` the result of `words.sortedBy { it.length }` · `F:` a list sorted by name when the price was meant · `B:` sort descending | Android: a contacts list sorted by name, a feed sorted by time. Assumes Kotlin language: `kotlin/lambdas/lambda-syntax`. ⚠️ VERIFY: Kotlin's sort functions are documented as stable (kotlinlang.org "Ordering"). |
| `simple-sorts` | easy | "How can repeatedly swapping neighbours put a list in order, and why does it get slow for big lists?" | Sorting a hand of playing cards by picking one at a time and sliding it into place. | "Swapping neighbours is fine for any size of list." | `sim:` balls on a slanted shelf that roll into place one swap at a time; a step counter runs for 10, 100 and 1000 balls and a bar grows like n². | `P:` the list after one pass of bubble sort · `F:` an inner loop that runs one step too far · `B:` sort an already-sorted list and count the steps | Android: a short list that stays in order while new items arrive, such as recent chats. Assumes Kotlin language: `kotlin/control-flow/ranges-and-for-loops`. |
| `merge-sort` | medium | "Why is splitting a list in half and merging the halves faster than swapping neighbours?" | Two sorted decks of cards riffled together into one sorted deck. | "Splitting adds work, so it cannot be faster." | `sim:` balls fall into halves, then halves of halves; a conveyor merges two sorted trays into one; a step counter compares with `simple-sorts` for 1000 balls. | `P:` the merge of `[1, 4, 9]` and `[2, 3, 10]` · `F(ai):` a merge function that drops the leftover items when one half runs out · `B:` run it on 1000 items and compare steps | Android: sorting thousands of photos by date, or merging a local list with a server list that is already sorted. The cost proof is `daa/divide-and-conquer/merge-sort-cost`. Assumes Kotlin language: `kotlin/functions/declaring-functions`. |
| `quick-sort` | hard | "How does choosing a pivot split a list into a smaller side and a bigger side?" | A teacher picks one pupil as the marker: shorter pupils line up on the left, taller on the right, then each group repeats. | "Quick sort is always the quickest, whatever the data." | `sim:` balls and a movable pivot line; sorted input with the first ball as pivot gives lopsided groups, a middle pivot gives even ones. | `P:` the two groups after splitting `[5, 2, 8, 1]` around 5 · `F(ai):` a partition that loses items equal to the pivot · `B:` feed sorted data with the first item as pivot | Android: sorting an array of sensor readings on the JDK side uses a quick-sort-style routine. ⚠️ VERIFY against Android's Java library docs before saying so. |
| `sorting-in-the-standard-library` | easy | "What does `sortedBy` do for me, and when does a comparator cause a crash?" | Hiring a removal firm: you only write the label that says how the boxes should be ordered. | "I should write my own sort to make it faster." | `sim:` a sorting machine with a swappable rule tag (by name, by price, by both); the learner changes the tag and the shelf reorders. | `P:` the order after `sortedWith(compareBy({ it.age }, { it.name }))` · `F(ai):` `list.sortedBy { … }` whose result is thrown away · `F(ai):` a comparator that subtracts two numbers and overflows · `B:` write a comparator that contradicts itself | Android: sorted screen data, and the real crash "Comparison method violates its general contract" from inconsistent comparators. ⚠️ VERIFY the exception text and that object sorts use a stable merge-based routine (Android and JDK docs). Assumes Kotlin language: `kotlin/lambdas/map-filter-and-friends`. |

### searching · Searching

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `binary-search` | medium | "How can I find a name among a million sorted names in about 20 steps?" | Opening a paper dictionary at the middle and deciding which half to keep. | "Binary search works on any list." | `sim:` a long ruler with a blade that cuts the search range in half at each guess; the learner picks the list size and the step counter grows by one for every doubling. | `P:` the positions checked when searching 7 in a sorted list of 10 · `F:` binary search run on an unsorted list · `B:` unsort the list | Android: jumping a long, sorted chat or log to a date, or looking up a version number in a sorted table. Assumes Kotlin language: `kotlin/control-flow/ranges-and-for-loops`. |
| `binary-search-boundaries` | hard | "Why do so many hand-written binary searches skip the last item or loop forever?" | Walking a hotel corridor to find the first room with the light on, not just any lit room. | "If I find the value I am done, and duplicates do not matter." | `sim:` reuse `dsa/searching/binary-search` sim, with markers for low, high and middle; a warning flag lights when the markers cross or stop moving. | `F(ai):` a tidy search with `while (low < high)` that never checks the last item · `F(ai):` `low = mid` that loops forever · `P:` the result for the first 2 in `[1, 2, 2, 2, 3]` · `B:` search for a value smaller than every item | Android: "first message after this time" in a sorted conversation. Assumes Foundation: `foundation/debugging-mindset/find-a-bug-without-guessing`. |

### trees · Trees

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-tree` | easy | "What are the root, a child, a leaf and the height of a tree?" | A family tree: one ancestor at the top, each person has parents above and children below. | "A node in a tree can point back up and make a loop." | `sim:` a hanging mobile: each hook holds children; the learner picks the top bar and moving one node carries its whole branch along. | `P:` the height and the number of leaves of a drawn tree · `F:` a node whose child list contains the node itself · `B:` link a child back to the root | Android: a screen is a tree: a column holds rows, a row holds text and an icon; folders on storage form a tree too. Helps to know UI (Compose and Views): `ui/first-composable/what-is-a-composable`. Assumes Kotlin language: `kotlin/classes-and-objects/what-is-a-class`, `kotlin/collections/list-vs-mutable-list`. |
| `walking-a-tree` | medium | "In which order can I visit every node, and why does the order matter?" | Exploring a cave system with a ball of string: go deep, come back, try the next tunnel. | "There is only one correct order to visit a tree." | `sim:` a ball rolls around the mobile; a switch picks "before children", "between children" or "after children" and the visit numbers change. | `P:` before, between and after orders of a five-node tree · `F:` a recursive walk that forgets the right child · `B:` add a node and re-predict the order | Android: the UI system goes through a screen's tree to measure and draw parents and children in order. ⚠️ VERIFY wording at developer.android.com before the leaf goes live. Assumes Kotlin language: `kotlin/functions/declaring-functions`. |
| `level-order-walk` | medium | "How do I visit a tree one level at a time, and what does the queue do?" | A lift stopping at one floor at a time before moving to the next. | "A walk down a tree must always go deep before wide." | `sim:` ripples spread from the root with a queue shown at the side; the learner swaps the queue for a stack and the walk turns depth-first. | `P:` the visit order of a seven-node tree · `F:` a stack used where a queue was needed · `B:` swap the queue for a stack | Android: an "expand one level" folder or comment view, and finding the nearest nested item. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |

### bst · Binary Search Trees

Flags: phase=4

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `bst-search-and-insert` | medium | "Why does a search tree find a value by looking at only a few nodes?" | A guessing game at every junction: a sign points "lower" or "higher". | "A search must look at every node in the tree." | `sim:` a pegboard: a ball dropped at the top goes left or right at each peg by comparison; dropping new balls builds the tree. | `P:` the path taken to find 6 in a given tree · `F:` an insert that puts smaller values on the wrong side · `B:` insert 1 to 7 in order | Android: sorted maps keep their keys in order, and database indexes keep searches fast with tree-shaped structures. ⚠️ VERIFY against developer.android.com and sqlite.org before saying so. Assumes Kotlin language: `kotlin/null-safety/nullable-types`. |
| `bst-shape-and-balance` | medium | "Why does adding already-sorted data make a search tree slow?" | A corridor with all the doors on one side, versus a building with branching hallways. | "A search tree is always fast." | `sim:` reuse `dsa/bst/bst-search-and-insert` sim, with a sorted stream of balls that builds a leaning chain; a "rebalance" button reshapes it. | `P:` the height after inserting 1 to 5 versus 3, 1, 5, 2, 4 · `F(ai):` an "indexed" structure built from sorted rows that degenerates into a chain · `B:` insert sorted data | Android: why library maps rebalance themselves, and a contact index that slows down after an import that is already sorted. ⚠️ VERIFY: `TreeMap` is a red-black tree in the Android reference. Rotations stay in `## Later`. |

### heaps · Heaps and Priority Queues

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `priority-queue-idea` | easy | "How does the most urgent item always come out first, even if it arrived last?" | A hospital emergency room: the most urgent patient is seen first, not the first to arrive. | "A priority queue keeps the whole list sorted." | `sim:` a waiting room of patient balls with urgency numbers beside a plain queue; the learner admits one patient at a time and compares who comes out. | `P:` the order out for tasks with priorities 3, 1, 2 · `F(ai):` a scheduler that sorts the whole list every time to find the top task · `B:` add two tasks with equal priority | Android: a download or to-do list that always shows the most urgent item, and a "5 nearest places" result. Helps to know Android core and lifecycles: `android-core/services-and-notifications/what-is-a-notification`. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |
| `how-a-heap-works` | medium | "How can a plain array keep the most urgent item at the front without sorting?" | A jar of balls where lighter balls float to the top and heavier ones sink. | "A heap is a fully sorted tree." | `sim:` a pyramid jar; a new ball rises past bigger parents (bubble up); when the top leaves, the last ball drops in and sinks; the array view fills in beside it. | `P:` the array after inserting 5, 3, 8, 1 · `F:` a parent index formula that is off by one for a zero-based array · `B:` remove the top repeatedly and watch a sorted order appear | Android: finding the 5 nearest places or the 10 slowest frames without sorting everything. ⚠️ VERIFY: `PriorityQueue` is a JVM class and is not in KMP common code. Assumes Kotlin language: `kotlin/collections/arrays-vs-lists`. |

### graphs · Graphs

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-graph` | easy | "How do I read a graph drawn as dots (nodes) joined by lines (edges)?" | A metro map: stations joined by tracks, with some lines forming loops. | "A graph is a chart with bars or lines." | `sim:` balls (nodes) joined by springs (edges); dragging one ball pulls its neighbours; the learner adds an edge that closes a loop. | `P:` how many neighbours node C has in a drawn graph · `F:` tree code that assumes one parent and breaks on a loop · `B:` add an edge that creates a loop | Android: an app's screens and the moves between them form a navigation graph. Helps to know UI (Compose and Views): `ui/navigation/what-is-navigation`. Assumes Kotlin language: `kotlin/collections/maps`. |
| `storing-a-graph` | easy | "How do I store who is connected to whom in a Map?" | An address book listing each person's friends, versus a big grid with a tick for every pair of people. | "A graph must be stored as a grid of ticks." | `sim:` reuse `dsa/graphs/what-is-a-graph` sim, with a switch between "friends list" and "grid view"; a sandbag grows with the grid for 1000 nodes with few edges. | `P:` the neighbours returned by a lookup in a `Map<String, List<String>>` · `F:` an undirected edge stored in only one direction · `B:` add 1000 nodes with few edges | Android: a map from each screen to the screens it can open. Assumes Kotlin language: `kotlin/collections/maps`. |
| `breadth-first-search` | medium | "How many taps does it take from the home screen to Settings?" | Ripples spreading from a stone dropped in a pond: every point at one distance is reached before the next ring. | "Breadth-first search finds the cheapest route on a map." | `sim:` ripples spread over the spring graph with a queue at the side and visited marks on the balls; removing the visited marks makes the ripples chase their own tail. | `F(ai):` a clean BFS with no visited set that loops forever on a cycle · `P:` the hops from A to F in a drawn graph · `B:` delete a node and re-run | Android: counting taps between screens in a navigation graph, "friends of friends" suggestions. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |
| `depth-first-search` | medium | "How do I explore every path to its end before turning back, and what must I remember?" | Solving a maze with chalk: go down a corridor, mark each junction, step back at a dead end. | "Depth-first search finds the shortest path." | `sim:` reuse the BFS sim with a ball on a string that goes deep, leaves chalk marks and steps back at dead ends. | `F(ai):` a recursive DFS with no visited set that ends in a stack-overflow crash · `F(ai):` directed edges treated as two-way · `P:` the visit order in a drawn graph · `B:` add a cycle | Android: finding every screen reachable from a start screen, or every item that depends on a changed item. Assumes Kotlin language: `kotlin/collections/maps`. |
| `dependency-order` | hard | "In what order should tasks run when some must wait for others, and how do I spot a circle that makes it impossible?" | Getting dressed: socks before shoes, and no order exists if socks needed shoes and shoes needed socks. | "A list of dependencies always has a valid order." | `sim:` tasks as stones hung by ropes; free stones fall first and drag the next layer loose; a cycle keeps a knot of stones hanging. | `P:` a valid order for five tasks with arrows · `F(ai):` ordering code that ignores a cycle and returns a partial list without an error · `B:` add a dependency from A to B and B to A | Android: a build runs its tasks in dependency order and reports a circular dependency as an error; chained background work runs step by step. ⚠️ VERIFY wording in the Gradle and WorkManager docs. Helps to know Android core and lifecycles: `android-core/background-work/what-is-workmanager`. Assumes Kotlin language: `kotlin/collections/maps`. |

### tries · Tries

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-trie` | medium | "How can a search box suggest words after only two letters without scanning a whole dictionary?" | A signpost tree where each junction is one letter, so "ca" already points down the branch of cat, car and card. | "The app checks every word in the dictionary for each letter I type." | `sim:` letters fall through a pegboard whose pegs are letters; typing two letters narrows the board and every word below that point lights up. | `P:` the words under the prefix "ca" in a drawn trie · `F:` an insert that forgets to mark the end of a word so "car" is lost · `B:` insert "car" and then "card" | Android: search-as-you-type suggestions in a search bar or a contact search. Assumes Kotlin language: `kotlin/collections/maps`. |

### patterns · Everyday Patterns

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `two-pointers` | medium | "How can two markers walking a sorted list find a pair without checking every pair?" | Two friends starting at opposite ends of a bookshelf and walking toward each other. | "Finding a pair always needs two nested loops." | `sim:` two markers on a shelf of sorted balls move inward by the learner's push; a counter shows n steps against n × n for the nested version. | `P:` the pair found for a target of 9 in a drawn list · `F(ai):` two pointers run on unsorted data and return a wrong answer · `F:` a `<=` loop condition that uses one item twice · `B:` unsort the list | Android: merging a local sorted list with a server sorted list in one pass. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |
| `sliding-window` | medium | "How do I find the best stretch of a list in one pass instead of re-adding every stretch?" | A train window moving along a landscape: a new view enters at the front while an old one leaves at the back. | "For each stretch of k items I must add all k items again." | `sim:` a frame slides along a bar chart; the running total changes by plus the new bar and minus the old bar; a counter compares with the add-everything version. | `P:` the best three-day total in a drawn list · `F(ai):` a window that recomputes the sum from scratch at each step · `B:` set k equal to the list size | Android: the average of the last N frame times or sensor readings, and a "no more than 5 taps per second" check. Assumes Kotlin language: `kotlin/control-flow/ranges-and-for-loops`. |
| `prefix-sums` | easy | "How can I answer 'what is the total from item 3 to item 8?' instantly, again and again?" | Kilometre posts on a road: the distance between two posts is the difference of their numbers. | "Every range total needs a fresh loop." | `sim:` a road with cumulative markers; the learner drags two flags and the total appears as one subtraction with one tick counted. | `P:` the total from index 2 to 4 using a prefix list · `F:` an off-by-one at a range that starts at index 0 · `B:` ask 1000 range questions and count steps | Android: a running balance in an expenses list, or the total steps between two dates in a fitness view. Assumes Kotlin language: `kotlin/collections/list-vs-mutable-list`. |
| `choose-the-right-structure` | hard | "For this feature, which structure would I reach for, and what will it cost for 100, 1,000 and 100,000 items?" | A toolbox: you do not tighten a bolt with a hammer, and you pick the tool before you start. | "One structure, the list, can do every job well." | `sim:` a workbench with job cards ("find by key", "most urgent first", "undo", "order of arrival", "search by prefix", "fewest hops") and structure tiles; pairing them shows a step counter, and a wrong pair makes the load tip. | `P:` match six jobs to structures and give the cost · `F(ai):` a generated "recent searches" feature using `contains` and `removeAt(0)` on a list · `B:` raise the item count to 100,000 | Android: contact search, recent items, undo, "nearest 5 places" and "how many taps away". Synthesis leaf; fruit (last leaf, `docs/08` §12 F2). Assumes Kotlin language: `kotlin/collections/maps`. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `dsa/recursion/repeated-work-in-recursion` | Mini file explorer model | Plain Kotlin in the Playground, no Android APIs · a folder-and-file model with at least 3 levels of nesting · a recursive function that adds up sizes, and a `Map` that finds an item by its path · back and forward moves kept with two stacks · a step counter, not a timer, printed for 10, 100 and 1000 items · a note naming the one line most likely to hide an O(n²) cost | "Why one approach is slower than another, in numbers" (`docs/01` §6.2, check: count steps for 10, 100 and 1000 items). Extra fruit F3: ends the phase-3 segment, 22 leaves in. |
| `dsa/patterns/choose-the-right-structure` | Contact book that picks its structures | Plain Kotlin in the Playground · search-as-you-type, last 10 contacts opened, undo of a delete, nearest 5 by distance and "who knows whom, in how many hops" · for each feature, write the chosen structure and its step count for 100, 1,000 and 100,000 contacts · use a step counter in code, not a timer · one rejected choice explained in a sentence · no sorting inside a loop | Same §6.2 ability at branch level: choose a structure and defend it with step counts. Last leaf of the branch, 24 leaves after the first fruit. |

## Later

- Balanced trees (AVL and red-black rotations): the BST leaves only need to know that libraries rebalance for you.
- Segment trees and Fenwick trees: contest tools; prefix sums cover the app-level need.
- Skip lists: rare outside library code.
- Suffix arrays: specialist text search.
- Union-find in depth: not needed to read typical app code.
- Monotonic stack and deque tricks: interview patterns; the pattern leaves cover the main idea.
- Radix and counting sort: niche; the sorting stem covers the general-purpose sorts.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 16 stems, 46 leaves, all `shadow`. | +46 | Equal to the 46 estimate (band 37–55). Shortest paths and spanning trees are left to `daa`; graph traversal is here. The last leaf is a synthesis leaf, `choose-the-right-structure`, so the branch ends on a decision, not a minor topic. |
| 2026-10-18 | Sim share note (A2e review): every one of the 46 leaves has a `sim:` cell (100 %), above the 35–50 % guide in `docs/08` §6. Kept because each leaf is about memory layout, order or step counts, which is what a learner can push and count; `physics-designer` should check which sims can share one kit before `docs/05` sizes the workload. | 0 | Logged, no leaf cut. |
| 2026-10-18 | One-idea titles (A2e review, `docs/08` §3 split test): reworded `what-is-a-queue`, `what-sorting-means` (now about ties and stable order) and `what-is-a-graph`; slugs unchanged. The stack comparison stays in the queue sim; "a tree is one kind of graph" moves into the graph leaf's body. Fruit row 1 merged two constraints (7 → 6, F5). | 0 | No leaf added or removed; reworded only. |
