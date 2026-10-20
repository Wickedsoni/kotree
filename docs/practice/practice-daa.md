# practice: daa

## daa/analysis/best-worst-average-case
problems:
- LeetCode 28 | Find the Index of the First Occurrence in a String | Easy | https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/ | Naive substring search runs in O(n) on average text but degrades to O(n * m) on adversarial inputs like "aaaa...ab"
- HackerRank | Running Time of Algorithms | Easy | https://www.hackerrank.com/challenges/runningtime/problem | [Algorithms > Sorting] Shows Insertion Sort doing 0 shifts on sorted input (best case O(n)) and n(n-1)/2 shifts on reverse-sorted input (worst case O(n^2))
- LeetCode 81 | Search in Rotated Sorted Array II | Medium | https://leetcode.com/problems/search-in-rotated-sorted-array-ii/ | O(log n) average case degrades to O(n) worst case when duplicates (nums[low] == nums[mid] == nums[high]) hide which half is sorted
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Includes adversarial test cases that time out deterministic-pivot Quick Sort in its O(n^2) worst case
- LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Quickselect runs in O(n) average time but hits O(n^2) worst-case time without random pivot and 3-way partitioning
interview-questions:
- Why does linear search have O(1) best case, O(n) average case, and O(n) worst case, and why do interviews focus primarily on worst-case and average/amortized complexity?
- Give an example of an input that triggers the worst-case O(n) behavior of Search in Rotated Sorted Array II (with duplicates).
- How does randomizing an algorithm (such as picking a random pivot in Quick Sort) change the meaning of "worst-case input"?
- What is the difference between average-case time complexity (averaged over a probability distribution of inputs) and amortized time complexity (guaranteed average over any sequence of operations)?

## daa/analysis/big-o-omega-theta
problems:
- LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Demonstrates O(log n) upper bound, Omega(1) best-case lower bound, and Theta(log n) worst-case tight bound
- LeetCode 268 | Missing Number | Easy | https://leetcode.com/problems/missing-number/ | Runs in exact Theta(n) time because every element must be inspected once regardless of input order
- LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Single-pass Dutch National Flag with tight Theta(n) time and Theta(1) auxiliary space bounds
- LeetCode 148 | Sort List | Medium | https://leetcode.com/problems/sort-list/ | Merge sort guarantees a tight Theta(n log n) time bound across best, average, and worst cases
- LeetCode 41 | First Missing Positive | Hard | https://leetcode.com/problems/first-missing-positive/ | Achieves a tight Theta(n) time bound via cyclic swapping where each valid number lands in its target slot at most once
interview-questions:
- What is the exact difference between Big-O (asymptotic upper bound), Big-Omega (asymptotic lower bound), and Big-Theta (asymptotically tight bound)?
- Is it mathematically true that an O(n) algorithm is also O(n^2) and O(2^n)? Why do engineers casually say O(n) when they really mean Theta(n)?
- Why can we say Merge Sort is Theta(n log n) in all cases, whereas Insertion Sort is Omega(n) and O(n^2) across all inputs (so it has no single Theta bound for all inputs)?
- Why is any comparison-based sorting algorithm lower-bounded by Omega(n log n) comparisons in the worst case?

## daa/analysis/loop-invariants
problems:
- LeetCode 26 | Remove Duplicates from Sorted Array | Easy | https://leetcode.com/problems/remove-duplicates-from-sorted-array/ | Maintains the invariant that nums[0..k-1] holds all unique elements seen so far in sorted order
- LeetCode 283 | Move Zeroes | Easy | https://leetcode.com/problems/move-zeroes/ | Maintains the invariant that nums[0..slow-1] are non-zero in original order and nums[slow..fast-1] are all zeroes
- LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Classic Dijkstra 3-way partition invariant across four regions: [0..low-1] are 0s, [low..mid-1] are 1s, [mid..high] are unknown, and [high+1..n-1] are 2s
- LeetCode 53 | Maximum Subarray | Medium | https://leetcode.com/problems/maximum-subarray/ | Kadane's invariant: at the end of iteration i, currentSum is the maximum sum of any contiguous subarray ending at index i
- LeetCode 42 | Trapping Rain Water | Hard | https://leetcode.com/problems/trapping-rain-water/ | Two-pointer invariant guaranteeing that whichever side has the smaller max boundary is the true bottleneck for that pointer's cell
interview-questions:
- What are the three steps required to prove a loop invariant (Initialization, Maintenance, and Termination)?
- In Dijkstra's 3-way partitioning (Sort Colors), why do we increment `mid` after swapping with `low`, but NOT increment `mid` after swapping with `high`? Explain using the loop invariant.
- State the loop invariant for standard binary search (`while (low <= high)`) that proves the target cannot be outside `[low..high]` if it exists in the array.
- How does stating your loop invariant aloud in an interview help you eliminate off-by-one bugs before the interviewer points them out?

## daa/recurrences/writing-a-recurrence
problems:
- LeetCode 700 | Search in a Binary Search Tree | Easy | https://leetcode.com/problems/search-in-a-binary-search-tree/ | One recursive call on half a balanced tree plus O(1) comparison gives T(n) = T(n/2) + O(1)
- LeetCode 104 | Maximum Depth of Binary Tree | Easy | https://leetcode.com/problems/maximum-depth-of-binary-tree/ | Two recursive calls on subtrees plus O(1) max operation gives T(n) = 2T(n/2) + O(1) on a balanced tree
- LeetCode 50 | Pow(x, n) | Medium | https://leetcode.com/problems/powx-n/ | Rewriting x^n as (x^(n/2))^2 changes the recurrence from T(n) = T(n - 1) + O(1) to T(n) = T(n/2) + O(1)
- LeetCode 105 | Construct Binary Tree from Preorder and Inorder Traversal | Medium | https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/ | Contrasts T(n) = 2T(n/2) + O(n) (linear scan for root in inorder) with T(n) = 2T(n/2) + O(1) (precomputed HashMap lookup)
- LeetCode 46 | Permutations | Medium | https://leetcode.com/problems/permutations/ | Making n recursive calls of size n - 1 gives T(n) = n * T(n - 1) + O(1), which expands to O(n!) leaves
interview-questions:
- In the recurrence `T(n) = a * T(n / b) + f(n)`, what do `a`, `n / b`, and `f(n)` each represent in your code?
- Write the recurrence relation for a function that loops from `0` to `n-1` and inside the loop makes a recursive call on `n - 1`.
- How does the recurrence for `Construct Binary Tree from Preorder and Inorder Traversal` change when the input tree is a skewed line instead of a balanced tree?
- What is the recurrence relation for generating all subsets of an n-element set, and why does `T(n) = 2T(n - 1) + O(1)` evaluate to `O(2^n)` whereas `T(n) = 2T(n / 2) + O(1)` evaluates to `O(n)`?

## daa/recurrences/solving-recurrences
problems:
- LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Solves T(n) = T(n/2) + O(1) = Theta(log n) via unrolling or Master Theorem Case 2
- LeetCode 222 | Count Complete Tree Nodes | Easy | https://leetcode.com/problems/count-complete-tree-nodes/ | Solves T(n) = T(n/2) + O(log n) by summing log n work across log n levels to get O((log n)^2)
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Solves T(n) = 2T(n/2) + Theta(n) = Theta(n log n) where work at every level of the recursion tree equals n
- LeetCode 53 | Maximum Subarray | Medium | https://leetcode.com/problems/maximum-subarray/ | Divide-and-conquer solution follows T(n) = 2T(n/2) + Theta(n) = Theta(n log n), contrasting with Kadane's Theta(n)
- LeetCode 23 | Merge k Sorted Lists | Hard | https://leetcode.com/problems/merge-k-sorted-lists/ | Solves the recursion tree across k lists where each of the log_2(k) levels merges N total nodes in O(N log k)
interview-questions:
- Explain the three cases of the Master Theorem intuitively: what happens when the root work `f(n)` dominates, when the leaves `n^(log_b a)` dominate, and when every level does equal work?
- Solve `T(n) = 2T(n/2) + O(1)` using the recursion-tree method: how many levels are there, how much work is done at the leaves, and why is the answer `O(n)` instead of `O(log n)`?
- Why does the Master Theorem NOT apply directly to `T(n) = T(n - 1) + n` or `T(n) = T(n/3) + T(2n/3) + n`, and how do you solve those by unrolling or drawing the recursion tree?
- If an algorithm splits an array of size n into 2 halves, solves only 1 half recursively, and does O(n) partitioning work (`T(n) = T(n/2) + O(n)`), why does the geometric series sum to `O(n)` instead of `O(n log n)`?

## daa/divide-and-conquer/split-solve-combine
problems:
- LeetCode 169 | Majority Element | Easy | https://leetcode.com/problems/majority-element/ | Splits the array in half, finds each half's majority element, and counts occurrences only when the two halves disagree
- LeetCode 108 | Convert Sorted Array to Binary Search Tree | Easy | https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/ | Picks the middle element as the root and recursively builds the left and right subtrees from the two halves
- LeetCode 395 | Longest Substring with At Least K Repeating Characters | Medium | https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/ | Splits the string at any character whose total count is less than k and solves the remaining substrings recursively
- LeetCode 241 | Different Ways to Add Parentheses | Medium | https://leetcode.com/problems/different-ways-to-add-parentheses/ | Splits the expression at every operator, recursively computes all values of the left and right sides, and combines pairs
- LeetCode 218 | The Skyline Problem | Hard | https://leetcode.com/problems/the-skyline-problem/ | Divides buildings into two halves, computes each half's skyline recursively, and merges the two skylines in linear time
interview-questions:
- What is the structural difference between **Divide and Conquer** (like Merge Sort) and **Dynamic Programming** (like Fibonacci or Edit Distance)?
- When does splitting a problem into two subproblems of size `n/2` beat solving it directly in `O(n^2)`?
- In `Longest Substring with At Least K Repeating Characters`, why is a character that appears fewer than `k` times in the whole string guaranteed to never appear in any valid substring?
- Why is Divide and Conquer naturally suited for multi-core parallel execution (such as Java's `ForkJoinPool` or Kotlin coroutines)?

## daa/divide-and-conquer/merge-sort-cost
problems:
- LeetCode 88 | Merge Sorted Array | Easy | https://leetcode.com/problems/merge-sorted-array/ | Isolates the linear O(m + n) two-pointer combine step that powers one merge operation
- LeetCode 148 | Sort List | Medium | https://leetcode.com/problems/sort-list/ | Performs log_2(n) levels of halving and linear-time pointer merging on a singly linked list
- HackerRank | Merge Sort: Counting Inversions | Hard | https://www.hackerrank.com/challenges/ctci-merge-sort/problem | [Interview Preparation Kit > Sorting] Piggybacks on the O(n) merge step across log n levels to count all inversions in O(n log n)
- LeetCode 493 | Reverse Pairs | Hard | https://leetcode.com/problems/reverse-pairs/ | Adds a linear two-pointer scan (`nums[i] > 2L * nums[j]`) between the recursive sort and the merge step while keeping total cost O(n log n)
- LeetCode 327 | Count of Range Sum | Hard | https://leetcode.com/problems/count-of-range-sum/ | Applies merge-sort divide-and-conquer over the prefix sum array to count valid range sums in O(n log n)
interview-questions:
- Draw the recursion tree for Merge Sort on 8 elements: how many levels exist, how many merges happen at level `d`, and why does the work at every single level sum to `c * n`?
- Why does counting inversions via Merge Sort take O(n log n) time whereas checking all `(i, j)` pairs takes O(n^2) time?
- In `Reverse Pairs` (LeetCode 493), why does the two-pointer counting step before merging two sorted halves take O(n) time instead of O(n^2)?
- What is the total auxiliary space of top-down Merge Sort, and why isn't the memory usage O(n log n) even though O(n) temporary arrays are allocated across log n levels?

## daa/divide-and-conquer/find-the-median-without-sorting
problems:
- HackerRank | Find the Median | Easy | https://www.hackerrank.com/challenges/find-the-median/problem | [Algorithms > Sorting] Finds the exact middle value of an unsorted odd-length array using partitioning
- LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Implements Quickselect (`T(n) = T(n/2) + O(n) = O(n)` average) by recursing only into the partition containing index `n - k`
- LeetCode 973 | K Closest Points to Origin | Medium | https://leetcode.com/problems/k-closest-points-to-origin/ | Uses Quickselect partitioning on squared Euclidean distances to isolate the k closest points in O(n) average time
- LeetCode 347 | Top K Frequent Elements | Medium | https://leetcode.com/problems/top-k-frequent-elements/ | Uses Quickselect over unique element frequencies to partition the top k elements in O(n) average time
- LeetCode 324 | Wiggle Sort II | Medium | https://leetcode.com/problems/wiggle-sort-ii/ | Finds the median in O(n) with Quickselect and applies 3-way partitioning around the median
- LeetCode 4 | Median of Two Sorted Arrays | Hard | https://leetcode.com/problems/median-of-two-sorted-arrays/ | Finds the median of two already-sorted arrays in O(log(min(m, n))) by binary-searching the partition cut
interview-questions:
- Why does Quickselect take O(n) average time while Quick Sort takes O(n log n) average time, even though both use the exact same `partition()` function?
- Why does the geometric series `n + n/2 + n/4 + n/8 + ...` sum to `< 2n`?
- When finding the k largest elements of an array of size n, compare the time and space complexity of: (1) full sorting, (2) a Min-Heap of size k, and (3) Quickselect. When would you prefer the Min-Heap over Quickselect?
- What is the Median-of-Medians (BFPRT) algorithm, and why is randomized Quickselect almost always preferred over it in practice?

## daa/greedy/greedy-choice-idea
problems:
- LeetCode 455 | Assign Cookies | Easy | https://leetcode.com/problems/assign-cookies/ | Sorts children and cookies so each child is satisfied by the smallest cookie that fits
- LeetCode 860 | Lemonade Change | Easy | https://leetcode.com/problems/lemonade-change/ | Greedily gives back a $10 + $5 bill before three $5 bills because $5 bills are strictly more versatile for future customers
- HackerRank | Luck Balance | Easy | https://www.hackerrank.com/challenges/luck-balance/problem | [Interview Preparation Kit > Greedy Algorithms] Sorts important contests to greedily lose the k contests with the largest luck values
- LeetCode 55 | Jump Game | Medium | https://leetcode.com/problems/jump-game/ | Maintains only the furthest reachable index in a single O(n) greedy scan instead of O(n^2) DP
- LeetCode 134 | Gas Station | Medium | https://leetcode.com/problems/gas-station/ | Proves that if you run out of gas reaching station `j` from `i`, no station between `i` and `j` can be the valid start
- LeetCode 135 | Candy | Hard | https://leetcode.com/problems/candy/ | Resolves two-sided neighbor constraints using a left-to-right greedy pass followed by a right-to-left greedy pass
interview-questions:
- What two properties (**Greedy Choice Property** and **Optimal Substructure**) must a problem have for a greedy algorithm to guarantee the globally optimal answer?
- What is an **exchange argument**, and how do you use it to prove that a greedy choice is never worse than any alternative choice?
- Why do so many greedy algorithms start by sorting the input array?
- In `Lemonade Change`, prove why giving a `$10 + $5` for a `$20` payment is always at least as good as giving `$5 + $5 + $5`.

## daa/greedy/interval-scheduling
problems:
- LeetCode 252 | Meeting Rooms | Easy | https://leetcode.com/problems/meeting-rooms/ | Sorts intervals by start time to verify in O(n log n) whether any meeting starts before the previous one ends
- LeetCode 56 | Merge Intervals | Medium | https://leetcode.com/problems/merge-intervals/ | Sorts intervals by start time and greedily extends the current end time whenever intervals overlap
- LeetCode 435 | Non-overlapping Intervals | Medium | https://leetcode.com/problems/non-overlapping-intervals/ | Classic interval scheduling: sort by end time (or keep the smaller end time on conflict) to fit the maximum number of non-overlapping intervals
- LeetCode 452 | Minimum Number of Arrows to Burst Balloons | Medium | https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/ | Sorts balloons by right endpoint and shoots each arrow at the earliest ending unburst balloon
- LeetCode 253 | Meeting Rooms II | Medium | https://leetcode.com/problems/meeting-rooms-ii/ | Sorts by start time and uses a Min-Heap of end times to reuse the earliest freed room
- LeetCode 1235 | Maximum Profit in Job Scheduling | Hard | https://leetcode.com/problems/maximum-profit-in-job-scheduling/ | Demonstrates that when each interval has a different profit weight, pure greedy fails and you must combine sorting, DP, and binary search
interview-questions:
- When selecting the maximum number of non-overlapping meetings for one room (`Non-overlapping Intervals`), why must you sort by **earliest end time** rather than earliest start time or shortest duration? Give counterexamples for the other two.
- When merging overlapping intervals (`Merge Intervals`), why do we sort by **start time** instead of end time?
- In `Meeting Rooms II`, explain both the Min-Heap approach and the chronological sweep-line (two sorted arrays of `starts` and `ends`) approach.
- What changes when each meeting has a dollar profit attached to it (`Maximum Profit in Job Scheduling`) so that earliest-end-time greedy no longer works?

## daa/greedy/when-greedy-fails
problems:
- LeetCode 322 | Coin Change | Medium | https://leetcode.com/problems/coin-change/ | Classic greedy failure: for coins `[1, 3, 4]` and amount `6`, taking `4` first uses 3 coins (`4+1+1`) instead of 2 (`3+3`)
- LeetCode 279 | Perfect Squares | Medium | https://leetcode.com/problems/perfect-squares/ | Taking the largest square `9` first for `n = 12` gives `9+1+1+1` (4 squares) instead of `4+4+4` (3 squares)
- LeetCode 198 | House Robber | Medium | https://leetcode.com/problems/house-robber/ | Greedily robbing the house with the most cash first fails on `[5, 10, 6]` where `5 + 6 = 11 > 10`
- LeetCode 416 | Partition Equal Subset Sum | Medium | https://leetcode.com/problems/partition-equal-subset-sum/ | Sorting descending and greedily assigning numbers to the smaller bucket fails on `[3, 3, 3, 4, 5]`
- LeetCode 1553 | Minimum Number of Days to Eat N Oranges | Hard | https://leetcode.com/problems/minimum-number-of-days-to-eat-n-oranges/ | Greedily dividing whenever `n` is divisible by 2 or 3 fails unless you compare stepping down to a multiple of 2 vs a multiple of 3 with memoization
interview-questions:
- Why does the greedy algorithm (always take the largest coin `<= remaining`) work for standard US coins `[1, 5, 10, 25]`, but fail for `[1, 3, 4]`?
- In an interview, how can you quickly test whether your greedy idea is right or wrong before spending 20 minutes coding it?
- Explain the difference between the **Fractional Knapsack** problem (where greedy by value-per-weight ratio is optimal) and the **0/1 Knapsack** problem (where greedy fails).
- Give a small 3-item counterexample showing why sorting items by `value / weight` fails for the 0/1 Knapsack problem.

## daa/dynamic-programming/overlapping-subproblems
problems:
- LeetCode 509 | Fibonacci Number | Easy | https://leetcode.com/problems/fibonacci-number/ | Shows how computing `fib(n)` naively solves `fib(n-2)` twice, `fib(n-3)` three times, and `fib(2)` exponentially many times
- LeetCode 70 | Climbing Stairs | Easy | https://leetcode.com/problems/climbing-stairs/ | Stepping 1+2 and stepping 2+1 from step 0 both land on step 3, creating an identical remaining subproblem
- LeetCode 118 | Pascal's Triangle | Easy | https://leetcode.com/problems/pascals-triangle/ | Visualizes overlapping subproblems in combinations `C(n, k) = C(n-1, k-1) + C(n-1, k)`
- LeetCode 62 | Unique Paths | Medium | https://leetcode.com/problems/unique-paths/ | Moving Right-then-Down and Down-then-Right both arrive at `(1, 1)` with the exact same subgrid remaining
- LeetCode 96 | Unique Binary Search Trees | Medium | https://leetcode.com/problems/unique-binary-search-trees/ | Choosing root `i` splits `n` nodes into left subtree size `i - 1` and right subtree size `n - i`, repeating the same subtree counts across every root choice
interview-questions:
- What does **overlapping subproblems** mean, and how do you spot it by drawing the first 3 levels of a recursion tree?
- If a recursive function takes parameters `(index, remainingSum)`, how do you upper-bound the total number of distinct subproblems that can ever exist?
- Why does `Unique Paths` on an `m x n` grid have `(m + n - 2 choose m - 1)` total paths (exponential), yet only `m * n` distinct subproblems?
- If a recursive problem has `2^n` calls in its recursion tree and every single call has a distinct state (no overlapping subproblems), will adding a memoization table speed it up?

## daa/dynamic-programming/memoization
problems:
- LeetCode 746 | Min Cost Climbing Stairs | Easy | https://leetcode.com/problems/min-cost-climbing-stairs/ | Wraps a simple top-down recursive function with an `IntArray` cache initialized to `-1`
- LeetCode 198 | House Robber | Medium | https://leetcode.com/problems/house-robber/ | Memoizes `robFrom(index)` so each house index `0..n-1` is evaluated only once in O(n) time
- LeetCode 139 | Word Break | Medium | https://leetcode.com/problems/word-break/ | Memoizes `canSegment(start)` so suffixes that cannot be segmented are never re-explored
- LeetCode 494 | Target Sum | Medium | https://leetcode.com/problems/target-sum/ | Caches `(index, currentSum)` states to collapse `2^n` `+`/`-` branches into `O(n * sum)` time
- LeetCode 329 | Longest Increasing Path in a Matrix | Hard | https://leetcode.com/problems/longest-increasing-path-in-a-matrix/ | Shows where top-down memoization shines: the strictly increasing grid value condition guarantees a DAG with no cycles, so `dfs(r, c)` needs no topological sort first
interview-questions:
- When implementing top-down memoization in Kotlin, why is an `IntArray` initialized to `-1` (or a `BooleanArray` + `visited` array) much faster and lighter on memory than a `HashMap<Pair<Int, Int>, Int>`?
- In `Word Break`, what bug happens if you only store `true` results in your cache instead of distinguishing `unvisited`, `true`, and `false`?
- When is top-down memoization faster than bottom-up tabulation (for example, when only a sparse subset of states in the table is actually reachable)?
- Why can't you memoize a recursive DFS on a graph that has **cycles** unless you change the state definition?

## daa/dynamic-programming/tabulation
problems:
- LeetCode 70 | Climbing Stairs | Easy | https://leetcode.com/problems/climbing-stairs/ | Bottom-up loop from `2..n` and space optimization from an `O(n)` array down to two `Int` variables (`prev1`, `prev2`)
- LeetCode 64 | Minimum Path Sum | Medium | https://leetcode.com/problems/minimum-path-sum/ | Fills a 2D DP table iteratively from top-left to bottom-right and optimizes space to a single 1D row
- LeetCode 300 | Longest Increasing Subsequence | Medium | https://leetcode.com/problems/longest-increasing-subsequence/ | Fills `dp[i]` from `0..n-1` by scanning all previous `j < i` where `nums[j] < nums[i]`
- LeetCode 221 | Maximal Square | Medium | https://leetcode.com/problems/maximal-square/ | Computes `dp[r][c] = 1 + minOf(dp[r-1][c], dp[r][c-1], dp[r-1][c-1])` bottom-up
- LeetCode 312 | Burst Balloons | Hard | https://leetcode.com/problems/burst-balloons/ | Interval DP where bottom-up tabulation must iterate by increasing subarray **length** (`len = 1..n`) so smaller sub-intervals are ready before larger ones
interview-questions:
- How do you determine the correct loop iteration order (left-to-right, right-to-left, or by interval length) when converting a recurrence into a bottom-up DP table?
- How does **rolling array optimization** let you reduce a 2D `O(m * n)` DP table (like in `Unique Paths` or `Edit Distance`) down to `O(min(m, n))` auxiliary space?
- Compare Top-Down Memoization and Bottom-Up Tabulation across four dimensions: stack overflow risk, space optimization potential, ease of writing, and constant-factor speed.
- In `Burst Balloons`, why does thinking about which balloon in `[left..right]` is burst **last** make the subproblems independent, whereas thinking about which balloon is burst **first** makes the remaining balloons adjacent and dependent?

## daa/dynamic-programming/pick-items-within-a-limit
problems:
- HackerRank | Knapsack | Medium | https://www.hackerrank.com/challenges/unbounded-knapsack/problem | [Algorithms > Dynamic Programming] Unbounded knapsack picking integers (with repetition) to get as close to target sum `k` as possible
- LeetCode 416 | Partition Equal Subset Sum | Medium | https://leetcode.com/problems/partition-equal-subset-sum/ | 0/1 Knapsack subset-sum check iterating capacity **backward** from `target` down to `num` so each item is used at most once
- LeetCode 322 | Coin Change | Medium | https://leetcode.com/problems/coin-change/ | Unbounded Knapsack minimizing the number of coins needed to reach `amount`
- LeetCode 518 | Coin Change II | Medium | https://leetcode.com/problems/coin-change-ii/ | Unbounded Knapsack counting unique **combinations** (outer loop over coins, inner loop forward over amount)
- LeetCode 474 | Ones and Zeroes | Medium | https://leetcode.com/problems/ones-and-zeroes/ | Two-dimensional 0/1 Knapsack where each string costs both `zeros` and `ones` against capacities `m` and `n`
- LeetCode 1235 | Maximum Profit in Job Scheduling | Hard | https://leetcode.com/problems/maximum-profit-in-job-scheduling/ | Take-or-skip DP over sorted intervals using binary search to jump to the latest non-conflicting job
interview-questions:
- In 1D space-optimized Knapsack DP, why does iterating the capacity loop **backward** (`for (w in capacity downTo weight)`) enforce **0/1 Knapsack** (each item used once), whereas iterating **forward** (`for (w in weight..capacity)`) gives **Unbounded Knapsack** (unlimited copies of each item)?
- Why does changing the loop nesting order in `Coin Change II` (coins outside vs amount outside) change what you count from **combinations** (`Coin Change II`, LC 518) to **permutations** (`Combination Sum IV`, LC 377)?
- Why do we say the `O(n * W)` DP for 0/1 Knapsack runs in **pseudo-polynomial** time rather than true polynomial time?
- If the array in `Partition Equal Subset Sum` has 100 elements but each element can be up to `10^9`, why does the boolean array DP approach fail, and what would you use instead?

## daa/dynamic-programming/compare-two-texts
problems:
- LeetCode 392 | Is Subsequence | Easy | https://leetcode.com/problems/is-subsequence/ | Warm-up two-pointer character matching before full 2D sequence alignment
- LeetCode 1143 | Longest Common Subsequence | Medium | https://leetcode.com/problems/longest-common-subsequence/ | Foundational 2D text comparison: `1 + dp[i-1][j-1]` on character match, else `max(dp[i-1][j], dp[i][j-1])`
- LeetCode 583 | Delete Operation for Two Strings | Medium | https://leetcode.com/problems/delete-operation-for-two-strings/ | Computes minimum deletions to make two words identical using `len1 + len2 - 2 * LCS`
- LeetCode 72 | Edit Distance | Medium | https://leetcode.com/problems/edit-distance/ | Classic Levenshtein distance modeling character insertion, deletion, and replacement in O(m * n)
- LeetCode 97 | Interleaving String | Medium | https://leetcode.com/problems/interleaving-string/ | 2D boolean DP verifying whether `s3[0..i+j-1]` can be formed by interleaving `s1[0..i-1]` and `s2[0..j-1]`
- LeetCode 115 | Distinct Subsequences | Hard | https://leetcode.com/problems/distinct-subsequences/ | Counts distinct ways target string `t` appears as a subsequence inside source string `s`
interview-questions:
- In `Edit Distance`, explain what each of the three transitions `dp[i - 1][j] + 1`, `dp[i][j - 1] + 1`, and `dp[i - 1][j - 1] + 1` represents in terms of editing `word1` into `word2`.
- Once you have filled the 2D table for `Longest Common Subsequence`, how do you trace backward from `dp[m][n]` to `(0, 0)` to reconstruct the actual LCS string (or a `git diff` view)?
- How does Android's `DiffUtil` (Myers' diff algorithm) relate to `Longest Common Subsequence` and `Edit Distance`, and why is Myers' algorithm faster than `O(N * M)` when two lists have only a few edits `D`?
- How do you reduce the space complexity of `Edit Distance` from `O(m * n)` to `O(min(m, n))` while keeping track of the diagonal value `dp[i - 1][j - 1]`?

## daa/backtracking/backtracking-idea
problems:
- LeetCode 78 | Subsets | Medium | https://leetcode.com/problems/subsets/ | Canonical choose-explore-unchoose (`path.add(x)` -> `dfs(i + 1)` -> `path.removeLast()`) state-space tree
- LeetCode 46 | Permutations | Medium | https://leetcode.com/problems/permutations/ | Backtracks over all ordering choices using a `used` boolean array (or in-place index swaps)
- LeetCode 17 | Letter Combinations of a Phone Number | Medium | https://leetcode.com/problems/letter-combinations-of-a-phone-number/ | Explores every keypad letter for the current digit and pops the character from `StringBuilder` upon return
- LeetCode 79 | Word Search | Medium | https://leetcode.com/problems/word-search/ | Temporarily marks `board[r][c] = '#'` before exploring 4 neighbors and restores the original character when backtracking
- LeetCode 131 | Palindrome Partitioning | Medium | https://leetcode.com/problems/palindrome-partitioning/ | Tries every palindrome prefix cut and undoes the addition to explore longer prefixes
interview-questions:
- Why must you write `result.add(ArrayList(currentPath))` (a defensive copy) at the base case of a backtracking function instead of `result.add(currentPath)`?
- Why do we reuse a single mutable `currentPath` list and undo changes (`removeLast()`) instead of allocating a brand-new list on every recursive call?
- In `Word Search`, why must you unmark `board[r][c]` when returning from `dfs(r, c, index)` even though in `Number of Islands` you never unmark visited cells?
- What is the time and space complexity of generating all `Subsets` (power set) vs all `Permutations` of an array of length `n`?

## daa/backtracking/pruning-the-search
problems:
- LeetCode 39 | Combination Sum | Medium | https://leetcode.com/problems/combination-sum/ | Sorts candidates so the loop can `break` immediately as soon as `candidate > remainingTarget`
- LeetCode 40 | Combination Sum II | Medium | https://leetcode.com/problems/combination-sum-ii/ | Sorts input and prunes duplicate sibling branches using `if (i > start && candidates[i] == candidates[i - 1]) continue`
- LeetCode 22 | Generate Parentheses | Medium | https://leetcode.com/problems/generate-parentheses/ | Prunes invalid bracket prefixes before they are ever built by enforcing `open < n` and `close < open`
- LeetCode 51 | N-Queens | Hard | https://leetcode.com/problems/n-queens/ | Places one queen per row and prunes attacked columns, main diagonals (`r - c`), and anti-diagonals (`r + c`) in O(1)
- LeetCode 37 | Sudoku Solver | Hard | https://leetcode.com/problems/sudoku-solver/ | Uses row, column, and 3x3 box bitmasks/boolean tables to prune illegal digits at every empty cell
interview-questions:
- In `Combination Sum II` (or `Subsets II`), why does the condition `if (i > start && nums[i] == nums[i - 1]) continue` skip duplicate combinations without forbidding the same value from being used at two different depths of the same combination?
- In `N-Queens`, why do all cells on the same main diagonal share the same `row - col` value, and all cells on the same anti-diagonal share the same `row + col` value?
- What is the difference between generating all `2^(2n)` bracket strings and filtering with `isValid()` versus pruning invalid branches during generation in `Generate Parentheses`?
- In constraint-satisfaction backtracking (like `Sudoku Solver`), why does choosing the empty cell with the **fewest valid candidates** first (Minimum Remaining Values heuristic) shrink the search tree by orders of magnitude?

## daa/graph-algorithms/shortest-path-dijkstra
problems:
- LeetCode 743 | Network Delay Time | Medium | https://leetcode.com/problems/network-delay-time/ | Textbook single-source Dijkstra with a `PriorityQueue` over directed non-negative edge weights
- HackerRank | Dijkstra: Shortest Reach 2 | Hard | https://www.hackerrank.com/challenges/dijkstrashortreach/problem | [Algorithms > Graph Theory] Computes shortest paths from a start vertex to all other vertices in a weighted undirected graph
- LeetCode 1514 | Path with Maximum Probability | Medium | https://leetcode.com/problems/path-with-maximum-probability/ | Adapts Dijkstra to use a Max-Heap that maximizes the product of edge probabilities along a path
- LeetCode 1631 | Path With Minimum Effort | Medium | https://leetcode.com/problems/path-with-minimum-effort/ | Minimax Dijkstra on a 2D grid where path cost is the maximum single-step height difference rather than a sum
- LeetCode 787 | Cheapest Flights Within K Stops | Medium | https://leetcode.com/problems/cheapest-flights-within-k-stops/ | Shows how a hop-count limit forces Dijkstra to prune only when both cost AND stops are worse, or use Bellman-Ford
- LeetCode 778 | Swim in Rising Water | Hard | https://leetcode.com/problems/swim-in-rising-water/ | Grid Dijkstra expanding the lowest elevation cell on the frontier first in O(N^2 log N)
interview-questions:
- Why does Dijkstra's algorithm fail when the graph contains **negative edge weights**, and why does the first time a node is popped from the Min-Heap guarantee its shortest distance only when all edge weights are `>= 0`?
- Since Java/Kotlin's `PriorityQueue` does not have an O(log V) `decreaseKey` operation, we push duplicate `(newDist, node)` pairs lazily. How do you skip stale entries in O(1) when popping from the heap (`if (d > dist[u]) continue`)?
- What is the time complexity of lazy Dijkstra using a binary heap on a graph with `V` vertices and `E` edges?
- When all edge weights in a graph are only `0` or `1`, how does **0-1 BFS** using an `ArrayDeque` beat Dijkstra's `O((V + E) log V)` down to `O(V + E)`?

## daa/graph-algorithms/shortest-path-with-a-heuristic
problems:
- LeetCode 1091 | Shortest Path in Binary Matrix | Medium | https://leetcode.com/problems/shortest-path-in-binary-matrix/ | Standard grid BFS that can be upgraded to A* using Chebyshev distance `max(|n-1-r|, |n-1-c|)` as the heuristic
- LeetCode 752 | Open the Lock | Medium | https://leetcode.com/problems/open-the-lock/ | State-space search where A* prioritizes lock states using the sum of circular digit distances to `target`
- LeetCode 127 | Word Ladder | Hard | https://leetcode.com/problems/word-ladder/ | Demonstrates directed frontier search (Bidirectional BFS or A* with character mismatch count heuristic) vs blind BFS
- LeetCode 773 | Sliding Puzzle | Hard | https://leetcode.com/problems/sliding-puzzle/ | 2x3 sliding board puzzle where the sum of Manhattan distances of tiles to their goal coordinates is an admissible A* heuristic
- LeetCode 675 | Cut Off Trees for Golf Event | Hard | https://leetcode.com/problems/cut-off-trees-for-golf-event/ | Repeated point-to-point shortest paths on a grid where A* with Manhattan distance (`|r1 - r2| + |c1 - c2|`) avoids exploring the opposite direction
interview-questions:
- In A* search (`f(n) = g(n) + h(n)`), what do `g(n)` and `h(n)` represent, and how does A* reduce to Dijkstra's algorithm when `h(n) = 0`?
- What does it mean for a heuristic `h(n)` to be **admissible** (never overestimates the true remaining cost), and what happens to the path returned by A* if `h(n)` overestimates?
- If you can move in **4 directions** (up, down, left, right) on a grid, why is **Manhattan distance** (`|dx| + |dy|`) admissible? If you can also move **diagonally** (8 directions) at cost 1, why is Manhattan distance NO LONGER admissible, and why must you use **Chebyshev distance** (`max(|dx|, |dy|)`) instead?
- Why does a map navigation app (like Google Maps) use A* / bidirectional heuristics instead of pure Dijkstra when routing from Chennai to Bengaluru?

## daa/hard-problems/p-np-in-plain-words
problems:
- LeetCode 416 | Partition Equal Subset Sum | Medium | https://leetcode.com/problems/partition-equal-subset-sum/ | NP-complete Subset Sum problem that is tractable only because the sum of numbers is small enough for pseudo-polynomial DP
- LeetCode 698 | Partition to K Equal Sum Subsets | Medium | https://leetcode.com/problems/partition-to-k-equal-sum-subsets/ | NP-hard bin-packing problem where `n <= 16` signals that exponential backtracking or bitmask DP is required
- LeetCode 847 | Shortest Path Visiting All Nodes | Hard | https://leetcode.com/problems/shortest-path-visiting-all-nodes/ | Unweighted Traveling Salesperson variant solved in `O(n^2 * 2^n)` using a `(node, visitedBitmask)` BFS for `n <= 12`
- LeetCode 943 | Find the Shortest Superstring | Hard | https://leetcode.com/problems/find-the-shortest-superstring/ | NP-hard Shortest Common Superstring reduced to Traveling Salesperson with `O(n^2 * 2^n)` bitmask DP for `n <= 12`
- LeetCode 1723 | Find Minimum Time to Finish All Jobs | Hard | https://leetcode.com/problems/find-minimum-time-to-finish-all-jobs/ | NP-hard minimum-makespan scheduling solved for `n <= 12` via pruned backtracking or subset-sum bitmask DP
interview-questions:
- Explain **P**, **NP**, **NP-Hard**, and **NP-Complete** in plain words: why is "easy to verify a given solution in polynomial time" the core definition of NP (not "not polynomial")?
- How does looking at the input constraint `n` in an interview problem (for example `n <= 12`, `n <= 20`, `n <= 2000`, `n <= 10^5`) immediately tell you whether the problem is NP-hard or solvable in polynomial time?
- Why takes `n!` or `2^n` operations to solve the exact Traveling Salesperson Problem (TSP), and why does `n = 30` stops make brute-force `30!` completely impossible even on a supercomputer?
- If your product manager asks your Android app to compute an optimal delivery route through 40 stops offline, what practical strategies (heuristics, greedy nearest-neighbor, 2-opt local search, or bitmask DP for small stops) would you propose?

## daa/choosing-an-approach/choose-a-strategy
problems:
- LeetCode 53 | Maximum Subarray | Medium | https://leetcode.com/problems/maximum-subarray/ | Can be solved via O(n^2) brute force, O(n log n) Divide & Conquer, or O(n) Kadane's DP/Greedy — trains picking the simplest optimal strategy
- LeetCode 45 | Jump Game II | Medium | https://leetcode.com/problems/jump-game-ii/ | Compares O(n^2) Dynamic Programming against O(n) Greedy BFS-window expansion
- LeetCode 322 | Coin Change | Medium | https://leetcode.com/problems/coin-change/ | Trains diagnosing why Greedy fails on arbitrary denominations and switching to Unbounded Knapsack DP
- LeetCode 139 | Word Break | Medium | https://leetcode.com/problems/word-break/ | Trains spotting repeated suffix work in Backtracking and upgrading to Memoization or BFS/DP
- LeetCode 787 | Cheapest Flights Within K Stops | Medium | https://leetcode.com/problems/cheapest-flights-within-k-stops/ | Trains choosing between BFS, Dijkstra, and Bellman-Ford DP when an extra hop constraint is introduced
- LeetCode 847 | Shortest Path Visiting All Nodes | Hard | https://leetcode.com/problems/shortest-path-visiting-all-nodes/ | Trains recognizing an NP-hard TSP structure from `n <= 12` and reaching for `O(n * 2^n)` bitmask state-space BFS
interview-questions:
- Walk through your mental checklist when you see a brand-new problem in an interview: how do problem goal (count all / find one / min-max / shortest path) and input size `n` narrow down the strategy?
- When a problem asks for the **minimum number of steps** to reach a state, how do you decide between **Greedy**, **BFS**, **Dijkstra**, and **Dynamic Programming**?
- When a problem asks for **all possible valid configurations** (like all valid IP addresses or all palindrome partitions) vs **how many valid configurations exist**, why does the first almost always use **Backtracking** while the second uses **Dynamic Programming**?
- How do you gracefully pivot in an interview if you start explaining a Greedy approach and realize 3 minutes in that a counterexample breaks it?

## Self-check
- LeetCode 1553 | Minimum Number of Days to Eat N Oranges | Verify exact problem number (1553) and Hard difficulty classification.
- LeetCode 1723 | Find Minimum Time to Finish All Jobs | Verify exact problem number (1723) and exact title wording.
- LeetCode 943 | Find the Shortest Superstring | Verify exact problem number (943).
- LeetCode 847 | Shortest Path Visiting All Nodes | Verify exact problem number (847).
- LeetCode 1631 | Path With Minimum Effort | Verify exact problem number (1631) and title casing.
- LeetCode 1514 | Path with Maximum Probability | Verify exact problem number (1514).
- LeetCode 1235 | Maximum Profit in Job Scheduling | Verify exact problem number (1235).
- LeetCode 675 | Cut Off Trees for Golf Event | Verify exact problem number (675).
- LeetCode 773 | Sliding Puzzle | Verify exact problem number (773).
- LeetCode 493 | Reverse Pairs | Verify exact problem number (493).