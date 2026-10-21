```yaml
branch: daa
bough: cs-theory
stems:
  - id: analysis
    title: Analysis of Algorithms
    why: A fresher needs to say exactly what a speed claim means and whether a loop is correct, before judging any clever algorithm.
    leaves:
      - id: best-worst-average-case
        title: Why can the same code be fast on one input and slow on another?
        difficulty: easy
        requires: []
        fruit: false
      - id: big-o-omega-theta
        title: What do O, Omega and Theta each promise?
        difficulty: medium
        requires: [daa/analysis/best-worst-average-case]
        fruit: false
      - id: loop-invariants
        title: How can I be sure a loop gives the right answer for every input, not just my tests?
        difficulty: medium
        requires: [daa/analysis/best-worst-average-case]
        fruit: false
  - id: recurrences
    title: Recurrences
    why: A recursive function's cost is an equation, and solving it explains why some splits are cheap and others explode.
    leaves:
      - id: writing-a-recurrence
        title: How do I turn a recursive function into an equation for its cost?
        difficulty: medium
        requires: [daa/analysis/big-o-omega-theta]
        fruit: false
      - id: solving-recurrences
        title: How do I get the cost out of T(n) = 2T(n/2) + n without guessing?
        difficulty: hard
        requires: [daa/recurrences/writing-a-recurrence]
        fruit: false
  - id: divide-and-conquer
    title: Divide and Conquer
    why: Splitting work into smaller copies is behind fast sorting, fast searching and work shared across threads.
    leaves:
      - id: split-solve-combine
        title: When does splitting a problem into smaller copies of itself pay off?
        difficulty: easy
        requires: [daa/recurrences/writing-a-recurrence]
        fruit: false
      - id: merge-sort-cost
        title: Why does merge sort take about n log n steps?
        difficulty: medium
        requires: [daa/divide-and-conquer/split-solve-combine, daa/recurrences/solving-recurrences]
        fruit: false
      - id: find-the-median-without-sorting
        title: How can I find the middle value without sorting everything?
        difficulty: hard
        requires: [daa/divide-and-conquer/split-solve-combine, daa/analysis/best-worst-average-case]
        fruit: false
  - id: greedy
    title: Greedy Algorithms
    why: Many everyday choices are made by always taking the best-looking option, and a fresher must know when that is safe.
    leaves:
      - id: greedy-choice-idea
        title: When can I take the best-looking choice at each step and still get the best total?
        difficulty: easy
        requires: [daa/analysis/loop-invariants]
        fruit: false
      - id: interval-scheduling
        title: How many meetings can fit in one room?
        difficulty: medium
        requires: [daa/greedy/greedy-choice-idea]
        fruit: false
      - id: when-greedy-fails
        title: Why does taking the biggest coin first sometimes use too many coins?
        difficulty: medium
        requires: [daa/greedy/interval-scheduling]
        fruit: false
  - id: dynamic-programming
    title: Dynamic Programming
    why: Remembering answers to smaller problems turns many impossible calculations into quick ones, and generated code often gets the remembering wrong.
    leaves:
      - id: overlapping-subproblems
        title: How do I spot a problem that keeps solving the same smaller problem?
        difficulty: easy
        requires: [daa/recurrences/writing-a-recurrence, daa/greedy/when-greedy-fails]
        fruit: false
      - id: memoization
        title: How does remembering answers turn a slow function into a fast one?
        difficulty: medium
        requires: [daa/dynamic-programming/overlapping-subproblems]
        fruit: false
      - id: tabulation
        title: How do I fill a table from the smallest case upward so no recursion is needed?
        difficulty: medium
        requires: [daa/dynamic-programming/memoization]
        fruit: false
      - id: pick-items-within-a-limit
        title: How do I choose which items to keep when space or time is limited?
        difficulty: hard
        requires: [daa/dynamic-programming/tabulation]
        fruit: false
      - id: compare-two-texts
        title: How different are two pieces of text, and which edits turn one into the other?
        difficulty: hard
        requires: [daa/dynamic-programming/tabulation]
        fruit: false
  - id: backtracking
    title: Backtracking
    why: Puzzles, validation and pattern matching try choices and undo them, and unchecked trying can freeze an app.
    leaves:
      - id: backtracking-idea
        title: How do I try every possibility but undo a step when it leads nowhere?
        difficulty: easy
        requires: [daa/divide-and-conquer/split-solve-combine]
        fruit: false
      - id: pruning-the-search
        title: How do I stop exploring paths that can no longer win?
        difficulty: medium
        requires: [daa/backtracking/backtracking-idea]
        fruit: false
  - id: graph-algorithms
    title: Graph Algorithms
    why: Route and path questions in apps are weighted graph questions, and a fresher should know what answers them.
    leaves:
      - id: shortest-path-dijkstra
        title: How does a map app find the fastest route?
        difficulty: medium
        requires: [daa/greedy/greedy-choice-idea]
        fruit: false
      - id: shortest-path-with-a-heuristic
        title: How can a guess about the distance left make the search faster?
        difficulty: medium
        requires: [daa/graph-algorithms/shortest-path-dijkstra]
        fruit: false
  - id: hard-problems
    title: Hard Problems
    why: Some problems have no known quick exact answer, and a fresher must recognise them instead of promising the impossible.
    leaves:
      - id: p-np-in-plain-words
        title: Why can't an app promise the best route through 30 stops?
        difficulty: medium
        requires: [daa/backtracking/pruning-the-search, daa/analysis/big-o-omega-theta]
        fruit: false
  - id: choosing-an-approach
    title: Choosing an Approach
    why: The skill that matters on the job is matching a new problem to a technique and checking that generated code did the same.
    leaves:
      - id: choose-a-strategy
        title: Is this problem split-and-combine, greedy, remembered subproblems, backtracking or a known hard one?
        difficulty: hard
        requires: [daa/greedy/when-greedy-fails, daa/dynamic-programming/tabulation, daa/backtracking/pruning-the-search, daa/hard-problems/p-np-in-plain-words]
        fruit: true
```

## Branch prerequisites

- Assumes Data structures and algorithms: `dsa/complexity/big-o-notation` — Big-O is introduced there as a picture; this branch makes it exact.
- Assumes Data structures and algorithms: `dsa/complexity/reading-complexity-in-code` — step counting is not re-taught.
- Assumes Data structures and algorithms: `dsa/recursion/repeated-work-in-recursion` — the lead-in to dynamic programming.
- Assumes Data structures and algorithms: `dsa/sorting/merge-sort` and `dsa/sorting/quick-sort` — their costs are proved here, so the algorithms are known first.
- Assumes Data structures and algorithms: `dsa/graphs/storing-a-graph` and `dsa/heaps/how-a-heap-works` — shortest paths use both.
- Assumes Kotlin language: `kotlin/collections/maps` — memo tables are Maps.
- Helps to know UI (Compose and Views): `ui/state-and-recomposition/remember` — `remember` is "memoization" on a screen.
- Helps to know Coroutines, Flow, threads: `concurrency/threads/running-work-on-another-thread` — the divide-and-conquer leaf recaps in one paragraph how work is shared across threads.
- Helps to know Profiling and optimisation: `profiling/jank-and-frames/frame-budget` — the median leaf recaps what frame-time percentiles are.
- Helps to know Security: `security/threat-thinking/who-would-attack-my-app` — the pruning leaf mentions that slow pattern matching can be abused.

Foundation is recommended first ("Start here"). The whole branch is phase 5, after `dsa` has opened (heaps and graphs sit in phase 5 in `dsa`).

Every theory leaf names where the idea shows up in a real app in its Notes cell (`Android:`), as `docs/01` §2.3 requires. Lines about library internals carry `⚠️ VERIFY` against official docs. Examples are plain Kotlin that runs in the browser editor.

One leaf is live since 21 Oct 2026; the rest are `shadow`.

## Stems

### analysis · Analysis of Algorithms

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `best-worst-average-case` | easy | "Why can one search take 1 step on one list and 1,000 steps on another list of the same size?" | Looking for your keys: sometimes they are in the first pocket, sometimes in the last place you check. | "An algorithm has one speed." | `sim:` the same linear search over a shelf of balls; the learner places the target at the front, middle or end and a bar shows the steps. | `P:` the steps used to find 5 in a drawn list, best and worst · `F(ai):` an "optimised" search that was only timed on a lucky input · `B:` pick the worst input for it | Android: an app tested only on a small tidy list feels fast, then slows on real data that hits the worst case. Assumes Data structures and algorithms: `dsa/complexity/big-o-notation`. |
| `big-o-omega-theta` | medium | "What do 'at most', 'at least' and 'exactly about' mean for an algorithm's cost?" | Trip times: "no more than 3 hours" (ceiling), "no less than 1 hour" (floor), "about 2 hours either way" (both). | "Big-O means the worst case." | `sim:` a curve with a ceiling line and a floor line; the learner drags the constants and sees when the curve stays inside the band for large n. | `P:` which of three statements are true for 3n + 2 · `F(ai):` a comment that claims O(n) above a nested loop · `B:` choose a ceiling that is too low | Android: judging claims such as "this is O(n log n)" in library docs, code reviews and AI-written comments. Assumes Data structures and algorithms: `dsa/complexity/reading-complexity-in-code`. |
| `loop-invariants` | medium | "What fact stays true after every turn of a loop, and how does it prove the answer?" | A shop till check: after each customer, the money in the till plus the receipts always equals the opening float. | "If my tests pass, the loop is correct." | `sim:` a balance scale that must stay level after each turn of a loop; a bug makes it tip on a particular input. | `P:` the value of the invariant after turn 3 of a running total · `F(ai):` a tidy "largest value" that starts at 0 and fails on all-negative lists · `F(ai):` a binary search whose invariant breaks on duplicates · `B:` feed it negative numbers | Android: cart totals, balances and maximums over user data, where one edge case makes the screen show a wrong number. Assumes Data structures and algorithms: `dsa/searching/binary-search-boundaries`. |

### recurrences · Recurrences

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `writing-a-recurrence` | medium | "What is the cost equation of a function that calls itself twice on half the list and then loops once?" | A reporting chain: the time to produce a report is the time each sub-team needs plus the time to staple it together. | "T(n) is a number, not an equation." | `sim:` a recursion tree with a cost label on each ball; the learner changes how many children each ball has and how much smaller they get. | `P:` write T(n) for a function that calls itself twice on n/2 and loops n times · `F:` a recurrence with n/2 where the code reduces n by 1 · `B:` change two calls to three | Android: nested layouts that make the system measure children more than once at each level are a classic explanation for slow screens. ⚠️ VERIFY wording in the Android layout-performance docs. Assumes Data structures and algorithms: `dsa/recursion/repeated-work-in-recursion`, `dsa/recursion/the-call-stack`. |
| `solving-recurrences` | hard | "How do I find the total cost of T(n) = 2T(n/2) + n by adding up the levels?" | Counting coins in a pyramid: the cost of one row times the number of rows. | "The master theorem is a formula to memorise." | `sim:` reuse `daa/recurrences/writing-a-recurrence` sim, with a bar per level; the learner switches between top-heavy, even and bottom-heavy shapes and the total changes. | `P:` the total by levels for T(n) = 2T(n/2) + n · `P:` which of three shapes a recurrence has · `F:` a level count that forgets the last level · `B:` change n/2 to n - 1 | Android: seeing why a library or generated routine that halves its work scales, and one that only trims one item does not. |

### divide-and-conquer · Divide and Conquer

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `split-solve-combine` | easy | "When does splitting a problem pay off, and what is the cost of putting the pieces together?" | Counting votes: each polling booth counts its own ballots and the totals are added at the end. | "Dividing always makes it faster." | `sim:` a big pile of balls split between trays that count at the same time, then a conveyor combines the counts; the learner makes the combine step expensive. | `P:` the steps to find the largest of 8 items by splitting · `F:` a split version whose combine step is missing · `B:` make the combine step as slow as the whole problem | Android: processing a big list or image in chunks on background workers and then joining the results. Helps to know Coroutines, Flow, threads: `concurrency/threads/running-work-on-another-thread`; one-paragraph recap. |
| `merge-sort-cost` | medium | "Why does merge sort take about n log n steps, when the simple sorts take n × n?" | A knock-out tournament: about log n rounds, and n players are handled in every round. | "Merge sort is as slow as the simple sorts because it still compares many items." | `sim:` reuse `daa/recurrences/writing-a-recurrence` recursion-tree sim, with n per level and log n levels; the learner compares 1,000 and 1,000,000 items against the simple-sort bar. | `P:` the number of levels for 1,024 items · `F(ai):` a merge sort that copies the sublists at every level and quietly uses far more memory · `B:` compare its step count with `simple-sorts` | Android: why the built-in sort stays smooth for 10,000 rows while a hand-written swap sort does not. Assumes Data structures and algorithms: `dsa/sorting/merge-sort`, `dsa/sorting/simple-sorts`. |
| `find-the-median-without-sorting` | hard | "How can I find the middle value, or the slowest 5%, without sorting everything?" | Finding the middle-height pupil: ask everyone to stand shorter or taller than a marker and only keep the side that holds the middle. | "To find the median I must sort first." | `sim:` reuse `dsa/sorting/quick-sort` sim, with the side that cannot hold the answer greyed out after each split. | `P:` which side is kept after each split of a drawn list · `F(ai):` a percentile function that sorts a copy of all the data every time a new value arrives · `B:` pick the worst pivot | Android: a performance screen that shows the median and the slowest-5% time of recent frames or requests. Helps to know Profiling and optimisation: `profiling/jank-and-frames/frame-budget`. Assumes Data structures and algorithms: `dsa/sorting/quick-sort`. |

### greedy · Greedy Algorithms

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `greedy-choice-idea` | easy | "When can I just take the best-looking choice at each step and be sure of the best total?" | A cashier giving change: biggest note first works with ordinary money. | "Greedy always gives the best answer." | `sim:` coins fall into a jar towards a target; the learner picks the coin set and sees an ordinary set always finish with the fewest coins. | `P:` the coins used for 63 with 25, 10, 5 and 1 · `F:` a greedy loop that never reduces the remaining amount · `B:` remove the 25 coin | Android: a shop or payment screen working out change; a cache that always throws out the item used longest ago is a greedy rule. Assumes Kotlin language: `kotlin/control-flow/ranges-and-for-loops`. |
| `interval-scheduling` | medium | "How many meetings can fit in one room, and which rule picks them?" | Booking one meeting room on a busy day. | "Pick the shortest meeting first (or the earliest start)." | `sim:` meetings as planks on a shelf; the learner picks a rule (earliest start, shortest, earliest finish) and counts how many planks fit; earliest finish fits the most. | `P:` the meetings chosen by earliest finish for a drawn day · `F(ai):` a scheduler sorted by start time that picks fewer meetings than possible · `B:` add one very long meeting | Android: a calendar or booking screen deciding which events to keep when times clash. Assumes Data structures and algorithms: `dsa/sorting/sorting-in-the-standard-library`. |
| `when-greedy-fails` | medium | "Why does the biggest-coin-first rule give 3 coins for 6 with coins 1, 3 and 4, when 2 coins is possible?" | Taking the nearest petrol station at every stop and ending up empty on a long desert stretch. | "If greedy works on my examples, it works every time." | `sim:` reuse `daa/greedy/greedy-choice-idea` sim, with the coin set 1, 3, 4 and target 6; the greedy jar fills with 4, 1, 1 while the best jar needs 3, 3. | `P:` greedy result and best result for 6 · `F(ai):` a confident change-maker that fails for an unusual coin set · `B:` find a counter-example for another rule | Android: a cart screen picking "the best mix of coupons or points", where a quick rule can leave savings unused. |

### dynamic-programming · Dynamic Programming

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `overlapping-subproblems` | easy | "How can I tell that a problem keeps solving the same smaller problem?" | Counting the ways to climb a staircase: reaching step 10 uses the answers for steps 9 and 8, which other steps also use. | "Every recursive problem is a dynamic programming problem." | `sim:` reuse `dsa/recursion/repeated-work-in-recursion` sim, beside a merge-sort call tree where no ball repeats; the learner marks which tree has red duplicates. | `P:` how many different subproblems `climb(6)` has · `F(ai):` a function labelled "dynamic programming" that remembers nothing · `B:` find the first repeated call | Android: a feature that recomputes the same sub-total over and over, such as the cost of each item combination in a bill. Assumes Data structures and algorithms: `dsa/recursion/repeated-work-in-recursion`. |
| `memoization` | medium | "How does a notebook of solved answers turn an exponential function into a fast one?" | A student's notebook of solved exercises: before working out a problem, look it up. | "Memoization means rewriting the whole algorithm." | `sim:` reuse `dsa/recursion/repeated-work-in-recursion` sim, with solved balls collapsing into notes on a shelf; a counter shows calls growing like 2ⁿ without notes and like n with them. | `P:` the number of calls for `fib(6)` with a notebook · `F(ai):` a memo `Map` created inside the function, so it resets on every call · `F(ai):` a memo keyed by the wrong value · `B:` remove the "have I seen this?" check | Android: `remember` on a screen keeps a result so it is not recomputed on every redraw; `by lazy` keeps a result after the first read. Helps to know UI (Compose and Views): `ui/state-and-recomposition/remember`. Assumes Kotlin language: `kotlin/collections/maps`. |
| `tabulation` | medium | "How do I fill a table from the smallest case upward and read the answer from the last cell?" | Building a multiplication table row by row, each cell using cells you already filled. | "Tabulation and memoization give different answers." | `sim:` a grid of cells filled by dropping balls from the left and from above, with arrows showing which cells each one needs. | `P:` the table for the fewest coins to make 7 from 1, 3 and 4 · `F(ai):` a table loop that starts at the wrong index and skips the base cell · `F(ai):` a table filled in an order that reads an empty cell · `B:` shrink the table to one row | Android: turning a slow recursive calculation, such as the cheapest set of items for a bill, into a loop that cannot run out of stack space on a phone. Assumes Data structures and algorithms: `dsa/recursion/the-call-stack`. |
| `pick-items-within-a-limit` | hard | "How do I choose which items to keep when storage or time is limited?" | Packing a backpack that has a weight limit: the most valuable item may not leave room for much else. | "Just take the most valuable items first." | `sim:` a backpack hung from a spring scale with a weight limit; the learner drops items in and a table below shows the best value for each weight. | `P:` the best value for three items and capacity 5 · `F(ai):` a table read that uses the same item twice · `B:` change the capacity and re-predict | Android: choosing which downloads or offline maps to keep within the free storage, or which tasks fit in a time window. Assumes Data structures and algorithms: `dsa/arrays/arrays-in-memory`. |
| `compare-two-texts` | hard | "How different are two pieces of text, and how do I find the longest part they share in the same order?" | Comparing two drafts of an essay with a highlighter: mark the longest run of words that appear in both, in the same order. | "Comparing texts means checking character by character from the start." | `sim:` two strings as rails with matching beads joined by strings; the learner moves a joining string and the table below fills in. | `P:` the length of the longest shared run for two short words · `F(ai):` a position-by-position compare in which one inserted letter makes everything differ · `B:` insert a letter at the start | Android: screens that work out which list rows changed so only those animate, and "did you mean" corrections. ⚠️ VERIFY: `DiffUtil` documents Eugene Myers's difference algorithm, which answers the same question as this table method but is not the same code; say so on the page. Helps to know UI (Compose and Views): `ui/lists/lazy-column`. Assumes Kotlin language: `kotlin/strings/strings-are-sequences`. |

### backtracking · Backtracking

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `backtracking-idea` | easy | "How do I try a choice, go deeper, and take the choice back when it fails?" | Walking a maze with a ball of string: at a dead end, roll the string back to the last junction. | "A choice, once made, cannot be taken back." | `sim:` a maze of pegs with a ball and a string; the learner presses "undo" and the ball returns to the last junction. | `P:` the order in which the arrangements of three letters are tried · `F(ai):` a permutation function that forgets to take the choice back from the shared list · `B:` remove the undo step | Android: a puzzle or Sudoku-style game screen, and text matching that tries alternatives and backs up. ⚠️ VERIFY wording on Android's regex engine at developer.android.com. Assumes Data structures and algorithms: `dsa/recursion/the-call-stack`. |
| `pruning-the-search` | medium | "How do I stop exploring a path as soon as I know it cannot win?" | A chess player who drops a move the moment she sees a losing reply. | "Pruning gives up correctness for speed." | `sim:` reuse `daa/backtracking/backtracking-idea` sim, with branches that grey out when a rule breaks; a counter compares the places visited with and without pruning. | `P:` the number of places visited with and without pruning for a drawn puzzle · `F(ai):` a pruning check placed after the recursive call, so it saves nothing · `B:` weaken the rule and watch the count grow | Android: a text pattern that freezes a screen on a certain input ("catastrophic backtracking"), a known slowness and abuse risk. Helps to know Security: `security/threat-thinking/who-would-attack-my-app`. ⚠️ VERIFY the term and example against official docs. |

### graph-algorithms · Graph Algorithms

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `shortest-path-dijkstra` | medium | "How does a map app find the fastest route when roads have different lengths?" | A grass fire spreading along roads of different lengths: it reaches the nearer towns first. | "Breadth-first search already finds the shortest route in kilometres." | `sim:` roads as pipes of different lengths with water flowing out of the start; the nearest town fills first and a small heap beside it shows which town is next. | `P:` the shortest distance from A to E in a drawn graph · `F(ai):` Dijkstra scanning a plain list for the next town and one run on a road with a negative length · `B:` add a negative road | Android: a route preview in a map or delivery app. Assumes Data structures and algorithms: `dsa/heaps/how-a-heap-works`, `dsa/graphs/storing-a-graph`. |
| `shortest-path-with-a-heuristic` | medium | "How can a guess about the distance left make a route search faster without losing the shortest route?" | Walking towards a tall landmark you can see, instead of fanning out in every direction. | "If the guess is a bit off, the route is wrong." | `sim:` reuse `daa/graph-algorithms/shortest-path-dijkstra` sim as two fires side by side, one leaning towards the goal; a counter shows how many places each explored. | `P:` the number of places explored by each search on a drawn grid · `F(ai):` a guess that overestimates, so the route found is not the shortest · `B:` set the guess to 0 and see Dijkstra | Android: a game or map screen where a character or marker finds a path across tiles. Assumes Data structures and algorithms: `dsa/graphs/breadth-first-search`. |

### hard-problems · Hard Problems

Flags: phase=5

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `p-np-in-plain-words` | medium | "Why can nobody promise the best route through 30 stops, when checking any one route is quick?" | A jigsaw puzzle: checking a finished picture takes a glance, but finding the arrangement can take forever. | "A cleverer programmer would find a fast exact answer." | `sim:` a slider for the number of stops; routes multiply like a growing shower of balls while a bar for checking one route stays tiny. | `P:` how many orders exist for 6 stops · `F(ai):` code that claims the best route through 25 stops by trying every order · `B:` raise the stops until it stalls | Android: route-planning and timetable screens that say "good route" rather than "best route". Assumes Data structures and algorithms: `dsa/recursion/the-call-stack`. |

### choosing-an-approach · Choosing an Approach

Flags: phase=5 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `choose-a-strategy` | hard | "Which technique fits this problem, and how do I check generated code used the right one?" | A mechanic matching a noise to its likely cause before opening the engine. | "One technique, such as dynamic programming, solves every problem." | `sim:` question cards drop through a pegboard of gates ("do pieces repeat?", "does the local best always win?", "can I undo a step?") and land in a technique bucket. | `P:` name the technique for six problem statements · `F(ai):` a generated solution that uses greedy on a problem with a limit · `F(ai):` brute force presented as "optimal" for 25 items · `B:` change one rule so the right technique flips | Android: reviewing generated code for a feature such as best coupons, route, text diff or scheduling, and saying whether the technique matches the problem. Synthesis leaf; fruit (last leaf, `docs/08` §12 F2). Assumes Data structures and algorithms: `dsa/patterns/choose-the-right-structure`. |

## Fruits

| Leaf | Capstone | Constraints (no steps, no solution) | Proves |
|---|---|---|---|
| `daa/choosing-an-approach/choose-a-strategy` | Technique review of three feature problems | Plain Kotlin in the Playground, no Android APIs · three problems of your own choice from an app (for example scheduling, best use of storage, comparing two texts) · for each, name the technique and give its step count for 10, 100 and 1,000 items, using a counter not a timer · for each, show one wrong technique failing on a small counter-example · implement one of the three and show its counter output · one note on whether any problem is a known hard one and what a "good enough" answer looks like | "Why one approach is slower than another, in numbers" (`docs/01` §6.2, check: count steps for 10, 100 and 1000 items), now at the level of choosing the technique. |

## Later

- Amortised analysis with the potential method: the growing-list lesson in the DSA branch gives the picture; the formal method is rare in app work.
- Network flow: specialist graph topic.
- String matching (KMP and Rabin-Karp): the library search is used, not rewritten.
- Approximation and randomised algorithms: named in the P and NP lesson, not taught.
- Minimum spanning trees (Kruskal and Prim): no everyday app feature to anchor them.
- Huffman coding: compression is a library job in app work.
- All-pairs shortest paths: beyond what a single-route screen needs.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 9 stems, 22 leaves, all `shadow`. | +22 | Equal to the 22 estimate (band 18–26). Differences from the sketch: spanning trees moved to `## Later` (no app anchor) and replaced by the A*-style heuristic leaf; a `loop-invariants` leaf added to analysis; a one-leaf stem `choosing-an-approach` added so the last leaf is a synthesis leaf, not the P and NP leaf (F2). Difficulty mix is 5 easy, 12 medium, 5 hard (23 % hard, under the 30 % cap), lighter on easy than the 40/45/15 guide because these topics start from Big-O. |
| 2026-10-18 | Difficulty mix corrected to match the YAML (A2e review): 5 easy, 12 medium, 5 hard; the line above said 13 and 4. Sim share note: every one of the 22 leaves has a `sim:` cell (100 %), above the 35–50 % guide in `docs/08` §6. Kept because each technique is a step-by-step process on small data that a learner can push and count; `physics-designer` should check which sims can reuse the `dsa` kit before `docs/05` sizes the workload. | 0 | Numbers fixed, no leaf changed or cut. |
