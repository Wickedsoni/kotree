# practice: dsa

## dsa/complexity/counting-steps
problems:
- LeetCode 1480 | Running Sum of 1d Array | Easy | https://leetcode.com/problems/running-sum-of-1d-array/ | Counts exact single-pass loop iterations over n elements
- LeetCode 1342 | Number of Steps to Reduce a Number to Zero | Easy | https://leetcode.com/problems/number-of-steps-to-reduce-a-number-to-zero/ | Directly counts operations as input halves or decrements
- HackerRank | Running Time of Algorithms | Easy | https://www.hackerrank.com/challenges/runningtime/problem | [Algorithms > Sorting] Counts exact shifts performed by insertion sort on concrete inputs
- LeetCode 268 | Missing Number | Easy | https://leetcode.com/problems/missing-number/ | Contrasts multi-pass counting with a single-pass O(n) arithmetic sum
- LeetCode 204 | Count Primes | Medium | https://leetcode.com/problems/count-primes/ | Shows how Sieve of Eratosthenes cuts inner-loop step counts from O(n^2) to O(n log log n)
interview-questions:
- Why do we count dominant operations instead of measuring wall-clock milliseconds when comparing two algorithms?
- If a loop runs n times and calls a helper that scans an array of size 100, how many total steps execute for n = 10, 100, and 1000?
- How does CPU caching make two O(n) loops with the same step count run at wildly different speeds on Android hardware?
- How many steps does a loop take if the counter doubles each iteration (i *= 2) instead of incrementing by 1?

## dsa/complexity/big-o-notation
problems:
- LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Canonical O(log n) runtime by halving the search space each step
- LeetCode 169 | Majority Element | Easy | https://leetcode.com/problems/majority-element/ | Contrasts O(n log n) sorting with O(n) Boyer-Moore voting and O(1) space
- LeetCode 50 | Pow(x, n) | Medium | https://leetcode.com/problems/powx-n/ | Reduces O(n) multiplications to O(log n) via binary exponentiation
- LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Demonstrates why a one-pass O(n) Dutch National Flag beats a two-pass O(n) count only in constant factors
- LeetCode 4 | Median of Two Sorted Arrays | Hard | https://leetcode.com/problems/median-of-two-sorted-arrays/ | Strict O(log(m + n)) requirement that forces discarding half of one array per step
interview-questions:
- What does O(n) guarantee about growth rate, and why does it ignore constants and lower-order terms?
- When can an O(n^2) algorithm outperform an O(n log n) algorithm in real production code?
- What is the time complexity of iterating over a Kotlin List of size n and calling contains() on a Set of size m inside the loop?
- Why is binary search O(log n) regardless of whether the logarithm base is 2 or 10?

## dsa/complexity/reading-complexity-in-code
problems:
- HackerRank | Bubble Sort | Easy | https://www.hackerrank.com/challenges/ctci-bubble-sort/problem | [Interview Preparation Kit > Sorting] Classic nested loop exhibiting O(n^2) comparisons and swaps
- LeetCode 283 | Move Zeroes | Easy | https://leetcode.com/problems/move-zeroes/ | Replaces a naive O(n^2) shift-on-zero loop with a two-pointer O(n) scan
- LeetCode 209 | Minimum Size Subarray Sum | Medium | https://leetcode.com/problems/minimum-size-subarray-sum/ | Shows why a nested while-loop inside a for-loop is still O(n) amortized when both pointers only move forward
- LeetCode 15 | 3Sum | Medium | https://leetcode.com/problems/3sum/ | Reduces O(n^3) triple loops to O(n^2) by sorting and walking two pointers
- LeetCode 84 | Largest Rectangle in Histogram | Hard | https://leetcode.com/problems/largest-rectangle-in-histogram/ | Monotonic stack where each bar is pushed and popped once, turning an apparent nested loop into O(n)
interview-questions:
- Does every function with a while-loop nested inside a for-loop run in O(n^2) time? Give a counterexample.
- Which Kotlin standard library calls on Strings or Lists secretly hide an O(n) scan inside what looks like a single line?
- What is the time complexity of a loop where the outer index i runs from 1 to n and the inner index j increments by i each step (j += i)?
- How do you spot accidental O(n^2) complexity when binding RecyclerView items or recomposing LazyColumn rows in Android?

## dsa/complexity/time-vs-space
problems:
- LeetCode 217 | Contains Duplicate | Easy | https://leetcode.com/problems/contains-duplicate/ | Trades O(n) HashSet memory to drop time from O(n log n) sort to O(n)
- LeetCode 1 | Two Sum | Easy | https://leetcode.com/problems/two-sum/ | Classic time-space tradeoff: O(n) HashMap vs O(n^2) brute force
- LeetCode 136 | Single Number | Easy | https://leetcode.com/problems/single-number/ | Eliminates O(n) hash table memory down to O(1) space using XOR while keeping O(n) time
- LeetCode 238 | Product of Array Except Self | Medium | https://leetcode.com/problems/product-of-array-except-self/ | Optimizes O(n) prefix/suffix arrays into O(1) extra space by reusing the output array
- LeetCode 41 | First Missing Positive | Hard | https://leetcode.com/problems/first-missing-positive/ | Reuses the input array as an in-place hash table to achieve O(n) time and O(1) auxiliary space
interview-questions:
- On memory-constrained Android devices, when would you deliberately choose an O(n log n) in-place algorithm over an O(n) algorithm that allocates O(n) memory?
- What is the difference between auxiliary space complexity and total space complexity?
- How does Kotlin's IntArray vs Array<Int> (or List<Int>) affect memory overhead when trading space for speed?
- Does recursion count toward space complexity even if you allocate no collections inside the function?

## dsa/arrays/arrays-in-memory
problems:
- HackerRank | Arrays - DS | Easy | https://www.hackerrank.com/challenges/arrays-ds/problem | [Data Structures > Arrays] Direct O(1) index addressing from both ends of a contiguous block
- HackerRank | 2D Array - DS | Easy | https://www.hackerrank.com/challenges/2d-array/problem | [Data Structures > Arrays] Constant-offset 2D indexing across hourglass coordinates
- LeetCode 121 | Best Time to Buy and Sell Stock | Easy | https://leetcode.com/problems/best-time-to-buy-and-sell-stock/ | Sequential contiguous memory scan maximizing cache locality
- LeetCode 189 | Rotate Array | Medium | https://leetcode.com/problems/rotate-array/ | In-place three-reversal trick exploiting O(1) random access without extra buffer allocation
- LeetCode 48 | Rotate Image | Medium | https://leetcode.com/problems/rotate-image/ | In-place 2D coordinate transposition and row reversal in O(1) extra memory
interview-questions:
- How does the CPU compute the exact memory address of array[500] in a single arithmetic instruction?
- Why is iterating row-by-row over a 2D array faster than iterating column-by-column?
- In Kotlin/JVM, what is the memory layout difference between IntArray and Array<Int>?
- Why must all elements in a primitive contiguous array have the exact same byte size?

## dsa/arrays/insert-and-delete-in-an-array
problems:
- LeetCode 27 | Remove Element | Easy | https://leetcode.com/problems/remove-element/ | Avoids O(n^2) repeated shifting by overwriting removed elements in a single O(n) pass
- LeetCode 26 | Remove Duplicates from Sorted Array | Easy | https://leetcode.com/problems/remove-duplicates-from-sorted-array/ | Compacts unique elements to the front in one pass instead of deleting in place repeatedly
- LeetCode 88 | Merge Sorted Array | Easy | https://leetcode.com/problems/merge-sorted-array/ | Fills from the back of the array so existing elements are never overwritten or shifted repeatedly
- HackerRank | Left Rotation | Easy | https://www.hackerrank.com/challenges/array-left-rotation/problem | [Data Structures > Arrays] Shows why shifting left d times one-by-one costs O(n*d) while index math costs O(n)
- LeetCode 1089 | Duplicate Zeros | Easy | https://leetcode.com/problems/duplicate-zeros/ | Simulates in-place middle insertions by counting shifts first and writing backward in O(n)
interview-questions:
- Why does calling list.removeAt(0) inside a loop over an ArrayList turn an O(n) loop into O(n^2)?
- Why is merging or inserting into an array with spare capacity at the end best done from right to left?
- If order does not matter, how can you delete the element at index i of an ArrayList in O(1) time?
- How does System.arraycopy work under the hood when you insert an item in the middle of a Kotlin MutableList?

## dsa/arrays/how-arraylist-grows
problems:
- HackerRank | Dynamic Array | Easy | https://www.hackerrank.com/challenges/dynamic-array/problem | [Data Structures > Arrays] Builds and queries multiple dynamically growing sequences
- LeetCode 1929 | Concatenation of Array | Easy | https://leetcode.com/problems/concatenation-of-array/ | Explicitly allocates a 2n backing array and copies elements across, mirroring a resize step
- LeetCode 118 | Pascal's Triangle | Easy | https://leetcode.com/problems/pascals-triangle/ | Pre-sizing vs dynamically appending rows of increasing capacity
- LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Combines a dynamic ArrayList with a HashMap to get O(1) amortized append and O(1) swap-with-last removal
- LeetCode 443 | String Compression | Medium | https://leetcode.com/problems/string-compression/ | Compresses a dynamic buffer in place using read and write cursors
interview-questions:
- If resizing an ArrayList copies all n elements in O(n) time, why do we still say add() at the end is O(1) amortized?
- Why do dynamic arrays grow by a multiplicative factor (such as 1.5x or 2x) instead of adding a fixed 100 slots each time?
- When should you pass an initialCapacity to ArrayList(initialCapacity) in Kotlin, and what happens if you over-allocate?
- Does removing elements from a Java/Kotlin ArrayList automatically shrink the underlying Object[] array?

## dsa/strings/text-building-cost
problems:
- LeetCode 1108 | Defanging an IP Address | Easy | https://leetcode.com/problems/defanging-an-ip-address/ | Linear-time StringBuilder accumulation instead of repeated immutable string concatenation
- LeetCode 344 | Reverse String | Easy | https://leetcode.com/problems/reverse-string/ | Mutating a mutable CharArray in-place with O(1) extra memory
- LeetCode 151 | Reverse Words in a String | Medium | https://leetcode.com/problems/reverse-words-in-a-string/ | Tokenizing and assembling words cleanly in O(n) time using StringBuilder
- LeetCode 394 | Decode String | Medium | https://leetcode.com/problems/decode-string/ | Nested StringBuilder stack that avoids quadratic string copying on repeated blocks
- LeetCode 68 | Text Justification | Hard | https://leetcode.com/problems/text-justification/ | Precise line-by-line StringBuilder padding without intermediate string allocations
interview-questions:
- Why are Strings immutable in Kotlin and Java, and how does that make `str += char` inside a loop O(n^2)?
- How does StringBuilder avoid copying the entire string on every append, and how is it similar to ArrayList?
- When does the Kotlin compiler optimize string templates (`"$a$b"`) into a single StringBuilder or invokedynamic call, and when does it fail (such as across loop iterations)?
- What is the String Pool (interning) on the JVM/ART, and why would mutable strings break it?

## dsa/strings/compare-text-by-characters
problems:
- LeetCode 242 | Valid Anagram | Easy | https://leetcode.com/problems/valid-anagram/ | Replaces O(n log n) sorting with an O(n) 26-slot frequency array
- LeetCode 383 | Ransom Note | Easy | https://leetcode.com/problems/ransom-note/ | Counts available character supply vs demand in O(n) time
- LeetCode 387 | First Unique Character in a String | Easy | https://leetcode.com/problems/first-unique-character-in-a-string/ | Two-pass character frequency histogram over a fixed alphabet
- LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | Uses a character-count signature as a hash key to group strings in O(n * k) without sorting
- LeetCode 438 | Find All Anagrams in a String | Medium | https://leetcode.com/problems/find-all-anagrams-in-a-string/ | Maintains a sliding 26-int frequency window to compare substrings in O(1) per step
- HackerRank | Sherlock and Anagrams | Medium | https://www.hackerrank.com/challenges/sherlock-and-anagrams/problem | [Algorithms > Strings] Counts unordered anagrammatic substring pairs using frequency signatures
interview-questions:
- When comparing whether two strings are anagrams, when should you use an IntArray(26) versus a HashMap<Char, Int>?
- How does Unicode (emojis, surrogate pairs, combining accents) complicate character-by-character comparison in Kotlin?
- Why is sorting two strings of length n to check for anagrams O(n log n) while counting characters is O(n) time and O(1) space for ASCII?
- How can you represent the set of lowercase ASCII letters present in a word using a single 32-bit Int bitmask?

## dsa/linked-lists/what-is-a-linked-list
problems:
- HackerRank | Print the Elements of a Linked List | Easy | https://www.hackerrank.com/challenges/print-the-elements-of-a-linked-list/problem | [Data Structures > Linked Lists] Basic pointer traversal from head until null
- LeetCode 876 | Middle of the Linked List | Easy | https://leetcode.com/problems/middle-of-the-linked-list/ | Fast and slow pointer traversal when random indexing is unavailable
- LeetCode 141 | Linked List Cycle | Easy | https://leetcode.com/problems/linked-list-cycle/ | Floyd's tortoise-and-hare pointer chasing to detect cycles in O(1) space
- LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Rewiring next pointers iteratively and recursively without losing the rest of the chain
- LeetCode 142 | Linked List Cycle II | Medium | https://leetcode.com/problems/linked-list-cycle-ii/ | Pointer distance algebra to locate the exact node where a cycle begins
- LeetCode 138 | Copy List with Random Pointer | Medium | https://leetcode.com/problems/copy-list-with-random-pointer/ | Deep-copying a pointer graph by interleaving cloned nodes in-place
interview-questions:
- Why does finding the k-th element in a linked list take O(k) time even though you know the exact index?
- How does Floyd's fast-and-slow pointer algorithm guarantee that the two pointers will meet inside a cycle rather than jumping over each other?
- Why do linked list nodes have poor CPU cache locality compared to an array?
- How much extra memory overhead does each node in a doubly linked list carry on a 64-bit JVM/ART runtime?

## dsa/linked-lists/insert-and-remove-nodes
problems:
- HackerRank | Insert a node at a specific position in a linked list | Easy | https://www.hackerrank.com/challenges/insert-a-node-at-a-specific-position-in-a-linked-list/problem | [Data Structures > Linked Lists] Splicing a new node in without losing the tail reference
- LeetCode 203 | Remove Linked List Elements | Easy | https://leetcode.com/problems/remove-linked-list-elements/ | Using a dummy sentinel head so removing the first node needs no special-case branch
- LeetCode 237 | Delete Node in a Linked List | Medium | https://leetcode.com/problems/delete-node-in-a-linked-list/ | Deleting a middle node in O(1) when given no head pointer by copying the next node's value
- LeetCode 19 | Remove Nth Node From End of List | Medium | https://leetcode.com/problems/remove-nth-node-from-end-of-list/ | Two pointers separated by n+1 steps plus a dummy node for clean single-pass removal
- LeetCode 707 | Design Linked List | Medium | https://leetcode.com/problems/design-linked-list/ | Implementing head, tail, and index-based insertion and deletion from scratch
- LeetCode 25 | Reverse Nodes in k-Group | Hard | https://leetcode.com/problems/reverse-nodes-in-k-group/ | Stress-tests pointer splicing across group boundaries without dropping links
interview-questions:
- What is the "dummy head" (sentinel node) pattern, and which edge cases does it eliminate when deleting nodes?
- In what order must you assign pointers when inserting newNode between prev and prev.next, and what breaks if you reverse those two lines?
- Why is deleting a node given a reference to it O(1) in a doubly linked list, but O(n) in a singly linked list (unless you copy values)?
- Why does the copy-next-node's-value trick (LeetCode 237) fail if the node to delete is the tail node?

## dsa/linked-lists/linked-list-vs-arraylist
problems:
- LeetCode 21 | Merge Two Sorted Lists | Easy | https://leetcode.com/problems/merge-two-sorted-lists/ | O(1) extra space splicing of existing nodes vs allocating a new merged array
- LeetCode 234 | Palindrome Linked List | Easy | https://leetcode.com/problems/palindrome-linked-list/ | Shows the extra work required (find middle + reverse half) when O(1) random access is missing
- LeetCode 143 | Reorder List | Medium | https://leetcode.com/problems/reorder-list/ | Interleaving ends in O(1) space via pointer manipulation vs O(n) space if copied to an ArrayList
- LeetCode 1472 | Design Browser History | Medium | https://leetcode.com/problems/design-browser-history/ | Directly compares doubly linked list truncation on visit() against a dynamic array with a logical end pointer
- LeetCode 146 | LRU Cache | Medium | https://leetcode.com/problems/lru-cache/ | The canonical real-world win for a doubly linked list: O(1) removal from the middle and move-to-front paired with a HashMap
- LeetCode 460 | LFU Cache | Hard | https://leetcode.com/problems/lfu-cache/ | Combines frequency buckets of doubly linked lists for strict O(1) eviction and promotion
interview-questions:
- If LinkedList has O(1) insertion and deletion, why is ArrayList almost always faster in practice even for workloads that insert in the middle?
- Name two real-world data structures where a doubly linked list genuinely beats an ArrayList.
- Why is ArrayDeque preferred over LinkedList when implementing a Queue or Stack in Kotlin/Java?
- What happens to garbage collection pressure on Android when you frequently add and remove items from a LinkedList vs an ArrayList?

## dsa/stacks/what-is-a-stack
problems:
- HackerRank | Maximum Element | Easy | https://www.hackerrank.com/challenges/maximum-element/problem | [Data Structures > Stacks] Tracks LIFO pushes, pops, and running maximums
- LeetCode 225 | Implement Stack using Queues | Easy | https://leetcode.com/problems/implement-stack-using-queues/ | Contrasts LIFO and FIFO ordering by building one from the other
- LeetCode 155 | Min Stack | Medium | https://leetcode.com/problems/min-stack/ | Augments each stack frame with the minimum-so-far to answer getMin() in O(1)
- LeetCode 150 | Evaluate Reverse Polish Notation | Medium | https://leetcode.com/problems/evaluate-reverse-polish-notation/ | Classic LIFO operand evaluation for postfix expressions
- LeetCode 739 | Daily Temperatures | Medium | https://leetcode.com/problems/daily-temperatures/ | Monotonic decreasing stack that resolves waiting items as soon as a warmer day appears
- LeetCode 42 | Trapping Rain Water | Hard | https://leetcode.com/problems/trapping-rain-water/ | Uses a monotonic stack to bound horizontal water layers between taller bars
interview-questions:
- How does an Android activity back stack or Jetpack Navigation back stack behave as a LIFO structure?
- Why should you use Kotlin's ArrayDeque instead of java.util.Stack when you need a stack?
- How can a stack maintain its minimum (or maximum) element in O(1) time without scanning the stack on pop()?
- What is a monotonic stack, and what kind of array problem ("next greater element") does it solve in O(n) time?

## dsa/stacks/matching-brackets
problems:
- LeetCode 20 | Valid Parentheses | Easy | https://leetcode.com/problems/valid-parentheses/ | Canonical LIFO bracket matcher verifying that the most recent open bracket closes first
- HackerRank | Balanced Brackets | Medium | https://www.hackerrank.com/challenges/balanced-brackets/problem | [Data Structures > Stacks] Checks multiple nested bracket pairs across batch inputs
- LeetCode 921 | Minimum Add to Make Parentheses Valid | Medium | https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/ | Counts unmatched open and close brackets using stack balance logic
- LeetCode 1249 | Minimum Remove to Make Valid Parentheses | Medium | https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/ | Pushes indices onto a stack so invalid parentheses can be stripped in O(n)
- LeetCode 856 | Score of Parentheses | Medium | https://leetcode.com/problems/score-of-parentheses/ | Uses stack depth to accumulate nested parenthesis scores
- LeetCode 32 | Longest Valid Parentheses | Hard | https://leetcode.com/problems/longest-valid-parentheses/ | Pushes indices onto a stack to measure the longest contiguous well-formed substring
interview-questions:
- Why can you validate a string with only one bracket type `(` and `)` using a single integer counter in O(1) space, whereas `([{` requires an O(n) stack?
- What two edge cases cause a naive bracket-matching implementation to crash or return true on invalid strings like `")("` or `"((("`?
- How is XML/HTML tag matching in an Android layout parser related to the balanced-brackets stack problem?
- How do you modify the stack in Valid Parentheses if you need to report the exact character index where the first mismatch occurred?

## dsa/queues/what-is-a-queue
problems:
- LeetCode 232 | Implement Queue using Stacks | Easy | https://leetcode.com/problems/implement-queue-using-stacks/ | Amortized O(1) FIFO queue built by reversing LIFO order across two stacks
- HackerRank | Queue using Two Stacks | Medium | https://www.hackerrank.com/challenges/queue-using-two-stacks/problem | [Data Structures > Queues] Processes enqueue, dequeue, and peek queries in strict arrival order
- LeetCode 622 | Design Circular Queue | Medium | https://leetcode.com/problems/design-circular-queue/ | Implements a fixed-size ring buffer with head and tail modulo arithmetic
- LeetCode 950 | Reveal Cards In Increasing Order | Medium | https://leetcode.com/problems/reveal-cards-in-increasing-order/ | Simulates a draw-and-move-to-bottom FIFO deck process using a queue of indices
- LeetCode 649 | Dota2 Senate | Medium | https://leetcode.com/problems/dota2-senate/ | Two-queue round-robin simulation where surviving voters re-enter the back of the line
interview-questions:
- How does a circular buffer (ring array) allow ArrayDeque to enqueue at the back and dequeue from the front in O(1) time without shifting elements?
- In Android, how does the main thread's MessageQueue order UI events and Runnables posted via a Handler?
- Why is implementing a queue with two stacks still O(1) amortized per operation even though one pop() may move n elements?
- In a ring buffer where head == tail can mean either completely empty or completely full, how do you distinguish the two states?

## dsa/queues/keeping-the-last-n-items
problems:
- LeetCode 933 | Number of Recent Calls | Easy | https://leetcode.com/problems/number-of-recent-calls/ | Slides a queue over timestamps, evicting entries older than t - 3000 ms
- LeetCode 346 | Moving Average from Data Stream | Easy | https://leetcode.com/problems/moving-average-from-data-stream/ | Maintains a bounded queue of the last k numbers and a running sum in O(1) per item
- LeetCode 641 | Design Circular Deque | Medium | https://leetcode.com/problems/design-circular-deque/ | Fixed-capacity double-ended ring buffer supporting O(1) eviction at either end
- LeetCode 362 | Design Hit Counter | Medium | https://leetcode.com/problems/design-hit-counter/ | Bounded circular bucket array vs queue for counting events in the last 300 seconds
- LeetCode 239 | Sliding Window Maximum | Hard | https://leetcode.com/problems/sliding-window-maximum/ | Monotonic deque that keeps only useful candidates from the last k items in O(1) amortized time
interview-questions:
- If you want to keep an in-memory crash-log buffer of only the last 100 log lines in an Android app, why is a fixed-size circular array better than a LinkedList?
- How do you update the running average of the last N items in O(1) time when the (N+1)-th item arrives?
- What happens if 10,000 events arrive in the same millisecond in a timestamp-based queue (like Hit Counter), and how do circular time-buckets bound the memory to O(window)?
- Why does Sliding Window Maximum use a double-ended queue (Deque) instead of a regular FIFO Queue or PriorityQueue?

## dsa/hashing/hashing-idea
problems:
- LeetCode 217 | Contains Duplicate | Easy | https://leetcode.com/problems/contains-duplicate/ | O(1) average set membership check to spot a repeat in one pass
- LeetCode 1 | Two Sum | Easy | https://leetcode.com/problems/two-sum/ | O(1) complement lookup in a HashMap as you scan the array
- HackerRank | Ransom Note | Easy | https://www.hackerrank.com/challenges/ctci-ransom-note/problem | [Interview Preparation Kit > Dictionaries and Hashmaps] Word-level frequency map lookup in O(1) per word
- LeetCode 359 | Logger Rate Limiter | Easy | https://leetcode.com/problems/logger-rate-limiter/ | Maps each message string to its last printed timestamp for O(1) deduplication
- HackerRank | Frequency Queries | Medium | https://www.hackerrank.com/challenges/frequency-queries/problem | [Interview Preparation Kit > Dictionaries and Hashmaps] Maintains two coordinated hash maps (value->count and count->howMany) for O(1) queries
- LeetCode 128 | Longest Consecutive Sequence | Medium | https://leetcode.com/problems/longest-consecutive-sequence/ | Uses a HashSet to start counting streaks only from streak beginnings, achieving O(n) time
interview-questions:
- Walk through the exact steps a HashMap takes from receiving a key object to finding its value in the underlying bucket array.
- Why does a HashMap allocate a power-of-two array size in Java/Kotlin, and how does `(n - 1) & hash` replace the slower modulo `%` operator?
- What is the load factor of a HashMap (0.75 by default on the JVM), and what happens when the map exceeds it?
- On Android, when would you consider SparseArray or ArrayMap instead of HashMap, and at what size do they become slower than HashMap?

## dsa/hashing/collisions
problems:
- LeetCode 705 | Design HashSet | Easy | https://leetcode.com/problems/design-hashset/ | Forces you to handle bucket collisions using separate chaining or open addressing
- LeetCode 706 | Design HashMap | Easy | https://leetcode.com/problems/design-hashmap/ | Implements put, get, and remove on colliding keys stored in the same bucket chain
- LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | Intentionally designs a canonical key so all anagrams collide into the same map entry
- LeetCode 249 | Group Shifted Strings | Medium | https://leetcode.com/problems/group-shifted-strings/ | Designs a custom shift-invariant hash key to bucket equivalent strings together
- LeetCode 535 | Encode and Decode TinyURL | Medium | https://leetcode.com/problems/encode-and-decode-tinyurl/ | Explores short-key generation and collision handling in a bi-directional lookup table
interview-questions:
- Compare separate chaining (linked lists / balanced trees in buckets) with open addressing (linear probing) when two keys hash to the same bucket.
- Why is a HashMap lookup O(1) on average, O(n) in the classic worst case, and O(log n) in modern Java/Kotlin when keys are Comparable?
- What is a hash-collision Denial-of-Service (HashDoS) attack, and how can an attacker craft JSON keys that freeze a server or app?
- Why does Java's HashMap XOR the upper 16 bits of `hashCode()` with the lower 16 bits (`h ^ (h >>> 16)`) before picking a bucket?

## dsa/hashing/hashcode-and-equals-contract
problems:
- LeetCode 349 | Intersection of Two Arrays | Easy | https://leetcode.com/problems/intersection-of-two-arrays/ | Relies on value equality and hashing to deduplicate elements across sets
- LeetCode 205 | Isomorphic Strings | Easy | https://leetcode.com/problems/isomorphic-strings/ | Bi-directional mapping where key equality and uniqueness must hold in both directions
- LeetCode 290 | Word Pattern | Easy | https://leetcode.com/problems/word-pattern/ | Highlights value equality (`equals`) vs reference equality (`===`) when mapping strings and tokens
- LeetCode 36 | Valid Sudoku | Medium | https://leetcode.com/problems/valid-sudoku/ | Encodes composite `(row, col, value)` tuples with proper equality and hashing in a single Set
- LeetCode 149 | Max Points on a Line | Hard | https://leetcode.com/problems/max-points-on-a-line/ | Requires reducing `(dy, dx)` fractions to a canonical GCD pair because `Double` slope keys suffer from floating-point equality bugs and `IntArray` lacks structural `hashCode`/`equals`
interview-questions:
- State the exact contract between `equals()` and `hashCode()`. What breaks in a HashSet if two objects are `equals() == true` but return different `hashCode()` values?
- If two objects return the exact same `hashCode()`, must `a.equals(b)` return true? What happens if every object returns `hashCode() = 42`?
- Why does putting a Kotlin `data class` with a `var` property into a `HashSet` and then mutating that `var` make the object impossible to find or remove?
- In Kotlin, why does a `data class` containing a `ByteArray` or `IntArray` break map lookups unless you override `equals()` and `hashCode()` with `contentEquals()` and `contentHashCode()`?

## dsa/recursion/what-is-recursion
problems:
- LeetCode 509 | Fibonacci Number | Easy | https://leetcode.com/problems/fibonacci-number/ | Simplest two-branch recursive definition with base cases n = 0 and n = 1
- LeetCode 231 | Power of Two | Easy | https://leetcode.com/problems/power-of-two/ | Base case plus recursive division step shrinking n toward 1
- LeetCode 21 | Merge Two Sorted Lists | Easy | https://leetcode.com/problems/merge-two-sorted-lists/ | Naturally recursive structure: pick the smaller head and merge the remaining list
- HackerRank | Recursive Digit Sum | Medium | https://www.hackerrank.com/challenges/recursive-digit-sum/problem | [Algorithms > Recursion] Reduces a huge number to its super digit by recursing until a single-digit base case
- LeetCode 50 | Pow(x, n) | Medium | https://leetcode.com/problems/powx-n/ | Halves the problem size on each recursive call: `pow(x * x, n / 2)`
- LeetCode 779 | K-th Symbol in Grammar | Medium | https://leetcode.com/problems/k-th-symbol-in-grammar/ | Traces a recursive binary tree structure back to row 1 without materializing the string
interview-questions:
- What two parts must every terminating recursive function have, and what happens at runtime if either is wrong?
- How do you prove by mathematical induction that a recursive function works for every input size n?
- When is a recursive solution clearer than an iterative loop, and when is an iterative loop safer on the JVM/Android?
- What is Kotlin's `tailrec` modifier, what requirement must the recursive call satisfy to use it, and what bytecode does the compiler generate?

## dsa/recursion/the-call-stack
problems:
- LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Compare the O(n) call-stack space of the recursive version with the O(1) space of the iterative version
- LeetCode 104 | Maximum Depth of Binary Tree | Easy | https://leetcode.com/problems/maximum-depth-of-binary-tree/ | Call-stack depth directly mirrors the height of the tree (O(log n) balanced vs O(n) skewed)
- LeetCode 394 | Decode String | Medium | https://leetcode.com/problems/decode-string/ | Can be solved either via the implicit call stack (recursion) or an explicit `ArrayDeque` stack
- LeetCode 636 | Exclusive Time of Functions | Medium | https://leetcode.com/problems/exclusive-time-of-functions/ | Literally simulates a single-threaded CPU call stack pushing on `"start"` and popping on `"end"`
- LeetCode 341 | Flatten Nested List Iterator | Medium | https://leetcode.com/problems/flatten-nested-list-iterator/ | Replaces eager recursive call-stack traversal with an explicit stack of iterators for lazy evaluation
interview-questions:
- What exact information is stored inside a single stack frame when function A calls function B?
- Why does deep recursion throw `StackOverflowError` on Android before exhausting the device's RAM, and roughly how deep can a JVM/ART call stack go?
- How do you convert any recursive function into an iterative function that cannot throw `StackOverflowError`?
- Why does the recursive solution to `Maximum Depth of Binary Tree` use O(n) space in the worst case even though you never allocate a list or array?

## dsa/recursion/repeated-work-in-recursion
problems:
- LeetCode 70 | Climbing Stairs | Easy | https://leetcode.com/problems/climbing-stairs/ | Naive recursion recomputes `ways(k)` exponentially; caching drops it from O(2^n) to O(n)
- LeetCode 1137 | N-th Tribonacci Number | Easy | https://leetcode.com/problems/n-th-tribonacci-number/ | Three-way branching recursion that explodes for n = 37 unless repeated subtrees are reused
- LeetCode 746 | Min Cost Climbing Stairs | Easy | https://leetcode.com/problems/min-cost-climbing-stairs/ | Overlapping recursive subproblems on step costs
- LeetCode 95 | Unique Binary Search Trees II | Medium | https://leetcode.com/problems/unique-binary-search-trees-ii/ | Builds subtrees recursively where identical ranges `[left, right]` appear across branches
- LeetCode 894 | All Possible Full Binary Trees | Medium | https://leetcode.com/problems/all-possible-full-binary-trees/ | Memoizing the recursive call for `n` nodes prevents recomputing identical subtree lists
- LeetCode 140 | Word Break II | Hard | https://leetcode.com/problems/word-break-ii/ | Without memoizing suffix results, pathological inputs force exponential re-exploration of the same suffix
interview-questions:
- Draw or describe the recursion tree for naive `fib(5)`. How many times is `fib(2)` computed, and why does the total call count grow as O(2^n)?
- Why does Merge Sort make two recursive calls per step and run in O(n log n) time, while naive Fibonacci makes two recursive calls per step and runs in O(2^n) time?
- How can you tell by looking at the arguments of a recursive function how many unique states can ever be called?
- What is the space cost of adding a memoization array or map to eliminate repeated work in recursion?

## dsa/sorting/what-sorting-means
problems:
- LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Sorts items with many duplicate keys, highlighting what happens to tied elements
- LeetCode 937 | Reorder Data in Log Files | Medium | https://leetcode.com/problems/reorder-data-in-log-files/ | Explicitly requires stable ordering for digit-logs and tie-breaking by identifier for letter-logs
- LeetCode 179 | Largest Number | Medium | https://leetcode.com/problems/largest-number/ | Defines a custom transitive comparator (`a + b` vs `b + a`) to establish a valid total ordering
- LeetCode 1366 | Rank Teams by Votes | Medium | https://leetcode.com/problems/rank-teams-by-votes/ | Multi-key sorting across 26 tie-breaking positions with alphabetical fallback
- LeetCode 1122 | Relative Sort Array | Easy | https://leetcode.com/problems/relative-sort-array/ | Sorts according to an external rank order rather than natural numeric order
interview-questions:
- What does it mean for a sorting algorithm to be **stable**, and why does stability matter when a user sorts a table by "Name" and then clicks "Sort by Department"?
- Which of Bubble Sort, Insertion Sort, Selection Sort, Merge Sort, Quick Sort, and Heap Sort are naturally stable?
- What mathematical properties (reflexivity, antisymmetry, transitivity) must a custom `Comparator` satisfy so `List.sortedWith()` does not throw `IllegalArgumentException: Comparison method violates its general contract!`?
- What is the difference between an in-place sort and an out-of-place sort?

## dsa/sorting/simple-sorts
problems:
- HackerRank | Bubble Sort | Easy | https://www.hackerrank.com/challenges/ctci-bubble-sort/problem | [Interview Preparation Kit > Sorting] Implements neighbour-swapping Bubble Sort and counts total swaps
- HackerRank | Insertion Sort - Part 1 | Easy | https://www.hackerrank.com/challenges/insertionsort1/problem | [Algorithms > Sorting] Shifts sorted elements right to insert the rightmost element into place
- HackerRank | Insertion Sort - Part 2 | Easy | https://www.hackerrank.com/challenges/insertionsort2/problem | [Algorithms > Sorting] Full O(n^2) Insertion Sort building the sorted prefix one card at a time
- LeetCode 147 | Insertion Sort List | Medium | https://leetcode.com/problems/insertion-sort-list/ | Implements Insertion Sort on a singly linked list by splicing nodes into a sorted prefix
- LeetCode 969 | Pancake Sorting | Medium | https://leetcode.com/problems/pancake-sorting/ | Selection-sort style logic: repeatedly bring the largest unsorted item to its final slot
interview-questions:
- Why does Insertion Sort run in O(n) time on an already-sorted or nearly-sorted array, whereas Selection Sort still takes O(n^2) comparisons?
- Why do production library sorts (like Timsort and Dual-Pivot Quicksort) switch to Insertion Sort when a subarray shrinks below ~32–47 elements?
- Which simple sort performs at most O(n) swaps even in the worst case, making it useful when writing to memory is much more expensive than reading?
- How can you modify Bubble Sort so that it terminates in O(n) time if the input array is already sorted?

## dsa/sorting/merge-sort
problems:
- LeetCode 88 | Merge Sorted Array | Easy | https://leetcode.com/problems/merge-sorted-array/ | Masters the two-pointer linear merge step that powers Merge Sort
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implements a full O(n log n) Merge Sort from scratch without triggering worst-case O(n^2) timeouts
- LeetCode 148 | Sort List | Medium | https://leetcode.com/problems/sort-list/ | Bottom-up or top-down Merge Sort on a linked list in O(n log n) time and O(1) / O(log n) space
- HackerRank | Merge Sort: Counting Inversions | Hard | https://www.hackerrank.com/challenges/ctci-merge-sort/problem | [Interview Preparation Kit > Sorting] Counts out-of-order pairs during the merge step in O(n log n)
- LeetCode 315 | Count of Smaller Numbers After Self | Hard | https://leetcode.com/problems/count-of-smaller-numbers-after-self/ | Tracks how many right-half elements jump ahead of each left-half element during Merge Sort
- LeetCode 23 | Merge k Sorted Lists | Hard | https://leetcode.com/problems/merge-k-sorted-lists/ | Generalizes two-way merging to k lists via divide-and-conquer pairwise merging in O(N log k)
interview-questions:
- Why does Merge Sort guarantee O(n log n) time in the best, average, and worst cases regardless of input order?
- Why does Merge Sort on an array require O(n) auxiliary space, while Merge Sort on a linked list can be done in O(1) auxiliary space (bottom-up)?
- During the `merge(left, right)` step, what slight condition (`<=` vs `<`) preserves or destroys the stability of Merge Sort?
- How is external Merge Sort used to sort a 100 GB log file when your machine only has 4 GB of RAM?

## dsa/sorting/quick-sort
problems:
- HackerRank | Quicksort 1 - Partition | Easy | https://www.hackerrank.com/challenges/quicksort1/problem | [Algorithms > Sorting] Isolates the core partitioning step around a chosen pivot
- LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | 3-way Dutch National Flag partition (`< pivot`, `== pivot`, `> pivot`) that prevents O(n^2) degradation on duplicates
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Tests randomized + 3-way Quick Sort against adversarial test cases (sorted and all-equal arrays)
- LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Quickselect: partitions like Quick Sort but recurses into only one side for O(n) average time
- LeetCode 973 | K Closest Points to Origin | Medium | https://leetcode.com/problems/k-closest-points-to-origin/ | Applies Quickselect partitioning on Euclidean distances to partition the top k points in O(n) average time
interview-questions:
- What input makes a naive Quick Sort (picking the first or last element as pivot) degrade to O(n^2) time and O(n) stack depth?
- How do randomized pivot selection (or median-of-three) and 3-way partitioning protect Quick Sort against sorted and all-duplicate inputs?
- If Merge Sort is guaranteed O(n log n) worst-case and Quick Sort is O(n^2) worst-case, why is Quick Sort often faster in practice on primitive arrays?
- Why is standard in-place Quick Sort unstable, and why do standard libraries use Dual-Pivot Quicksort for primitive arrays (`IntArray`) but Timsort for object arrays (`Array<T>`)?

## dsa/sorting/sorting-in-the-standard-library
problems:
- HackerRank | Mark and Toys | Easy | https://www.hackerrank.com/challenges/mark-and-toys/problem | [Interview Preparation Kit > Sorting] Uses standard library sort to greedily buy the cheapest items first
- LeetCode 1636 | Sort Array by Increasing Frequency | Easy | https://leetcode.com/problems/sort-array-by-increasing-frequency/ | Uses chained comparators (`compareBy<Int> { freq[it] }.thenByDescending { it }`)
- LeetCode 56 | Merge Intervals | Medium | https://leetcode.com/problems/merge-intervals/ | Sorts intervals by start time with `sortedBy { it[0] }` so overlapping intervals become adjacent
- LeetCode 406 | Queue Reconstruction by Height | Medium | https://leetcode.com/problems/queue-reconstruction-by-height/ | Sorts primarily by height descending and secondarily by k ascending before inserting
- LeetCode 354 | Russian Doll Envelopes | Hard | https://leetcode.com/problems/russian-doll-envelopes/ | Sorts by width ascending and height descending so equal widths cannot nest inside LIS
interview-questions:
- In Kotlin, what is the difference between `list.sortedBy { ... }` and `mutableList.sortBy { ... }` in terms of time, memory allocation, and return type?
- What algorithm backs `Arrays.sort(Object[])` / `List.sorted()` on the JVM (Timsort), and why does it run in O(n) time on partially sorted real-world data?
- Why does `IntArray.sort()` use Dual-Pivot Quicksort while `List<Int>.sorted()` uses Timsort?
- If your selector inside `sortedBy { expensiveCalculation(it) }` is slow, how many times is `expensiveCalculation` called for n items, and how can you reduce it to exactly n calls?

## dsa/searching/binary-search
problems:
- LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Exact-match binary search in a sorted array in O(log n)
- LeetCode 374 | Guess Number Higher or Lower | Easy | https://leetcode.com/problems/guess-number-higher-or-lower/ | Binary search over a continuous range `[1..n]` using an oracle API
- LeetCode 69 | Sqrt(x) | Easy | https://leetcode.com/problems/sqrtx/ | Binary search for the largest integer whose square does not exceed x
- LeetCode 74 | Search a 2D Matrix | Medium | https://leetcode.com/problems/search-a-2d-matrix/ | Treats a row-sorted 2D matrix as a virtual 1D sorted array of length `m * n`
- LeetCode 33 | Search in Rotated Sorted Array | Medium | https://leetcode.com/problems/search-in-rotated-sorted-array/ | Identifies which half is sorted at each step to maintain O(log n) search
- LeetCode 875 | Koko Eating Bananas | Medium | https://leetcode.com/problems/koko-eating-bananas/ | Binary search on the answer space (eating speed) using a monotonic feasibility check
interview-questions:
- Why does binary search take at most 20 comparisons on 1,000,000 items and at most 30 comparisons on 1,000,000,000 items?
- Why should you write `val mid = low + (high - low) / 2` (or `(low + high) ushr 1`) instead of `(low + high) / 2`?
- What does Kotlin's `list.binarySearch(target)` return when the target is **not** present in the list, and how do you extract the insertion index from that return value?
- How do you recognize when an optimization problem can be solved by "binary searching on the answer"?

## dsa/searching/binary-search-boundaries
problems:
- LeetCode 278 | First Bad Version | Easy | https://leetcode.com/problems/first-bad-version/ | Finds the exact first `true` boundary in a `[false, false, ..., true, true]` monotonic predicate
- LeetCode 35 | Search Insert Position | Easy | https://leetcode.com/problems/search-insert-position/ | Lower-bound binary search: first index `i` where `nums[i] >= target`
- LeetCode 34 | Find First and Last Position of Element in Sorted Array | Medium | https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/ | Pairs lower-bound (`first >= target`) and upper-bound (`first > target - 1`) boundary searches
- LeetCode 153 | Find Minimum in Rotated Sorted Array | Medium | https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/ | Boundary search using `while (low < high)` and `high = mid` without skipping the inflection point
- LeetCode 162 | Find Peak Element | Medium | https://leetcode.com/problems/find-peak-element/ | Uses `nums[mid] < nums[mid + 1]` boundary invariants to climb toward a local peak in O(log n)
- LeetCode 410 | Split Array Largest Sum | Hard | https://leetcode.com/problems/split-array-largest-sum/ | Finds the minimum feasible maximum subarray sum on a monotonic boundary
interview-questions:
- Compare the loop condition `while (low <= high)` with `while (low < high)`. When do you use `high = mid - 1` versus `high = mid`?
- Why does writing `low = mid` inside `while (low < high)` cause an infinite loop when `high == low + 1`, and how does `mid = low + (high - low + 1) / 2` fix it?
- How do you find the count of a duplicate value `x` in a sorted array of 10 million elements in O(log n) time?
- What loop invariant holds for `low` and `high` throughout a lower-bound (`first index >= target`) binary search?

## dsa/trees/what-is-a-tree
problems:
- LeetCode 2236 | Root Equals Sum of Children | Easy | https://leetcode.com/problems/root-equals-sum-of-children/ | Inspects a parent node and its direct left and right child references
- LeetCode 104 | Maximum Depth of Binary Tree | Easy | https://leetcode.com/problems/maximum-depth-of-binary-tree/ | Computes the height of a tree from root to deepest leaf
- LeetCode 100 | Same Tree | Easy | https://leetcode.com/problems/same-tree/ | Checks structural and value equality of two recursive hierarchies
- LeetCode 226 | Invert Binary Tree | Easy | https://leetcode.com/problems/invert-binary-tree/ | Recursively swaps child subtrees at every node in the hierarchy
- LeetCode 543 | Diameter of Binary Tree | Easy | https://leetcode.com/problems/diameter-of-binary-tree/ | Distinguishes longest path through a node (`leftHeight + rightHeight`) from the height returned to its parent
- LeetCode 236 | Lowest Common Ancestor of a Binary Tree | Medium | https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/ | Finds the deepest shared parent of two nodes in a hierarchy (just like finding the common parent `ViewGroup` of two Android `View`s)
interview-questions:
- What makes a connected graph a **tree**, and why does a tree with n nodes always have exactly n - 1 edges?
- Define root, leaf, parent, ancestor, depth of a node, and height of a tree.
- How is an Android View hierarchy (or Jetpack Compose UI node tree) structured as an N-ary tree, and why can a View only have one parent?
- What is the maximum and minimum number of nodes in a binary tree of height h?

## dsa/trees/walking-a-tree
problems:
- HackerRank | Tree: Preorder Traversal | Easy | https://www.hackerrank.com/challenges/tree-preorder-traversal/problem | [Data Structures > Trees] Visits `Root -> Left -> Right` to process parents before children
- LeetCode 94 | Binary Tree Inorder Traversal | Easy | https://leetcode.com/problems/binary-tree-inorder-traversal/ | Visits `Left -> Root -> Right` both recursively and with an explicit stack
- LeetCode 145 | Binary Tree Postorder Traversal | Easy | https://leetcode.com/problems/binary-tree-postorder-traversal/ | Visits `Left -> Right -> Root` so children are finished before their parent
- LeetCode 105 | Construct Binary Tree from Preorder and Inorder Traversal | Medium | https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/ | Shows how Preorder picks the root while Inorder splits the left and right subtrees
- LeetCode 297 | Serialize and Deserialize Binary Tree | Hard | https://leetcode.com/problems/serialize-and-deserialize-binary-tree/ | Encodes and reconstructs any binary tree using Preorder traversal with null markers
- LeetCode 124 | Binary Tree Maximum Path Sum | Hard | https://leetcode.com/problems/binary-tree-maximum-path-sum/ | Postorder bottom-up aggregation computing the best single-branch gain from each child before updating the global answer
interview-questions:
- Give one practical use case each for Preorder, Inorder, and Postorder traversal.
- Why does Android's layout measurement pass (`onMeasure`) naturally resemble a postorder/bottom-up tree walk when children wrap content?
- Why can you NOT uniquely reconstruct a general binary tree from Preorder and Postorder alone (without Inorder or null markers)?
- How do you implement an iterative Inorder traversal using an explicit `ArrayDeque` stack in Kotlin?

## dsa/trees/level-order-walk
problems:
- HackerRank | Tree: Level Order Traversal | Easy | https://www.hackerrank.com/challenges/tree-level-order-traversal/problem | [Data Structures > Trees] Classic single-queue breadth-first walk from top to bottom
- LeetCode 102 | Binary Tree Level Order Traversal | Medium | https://leetcode.com/problems/binary-tree-level-order-traversal/ | Snapshots `queue.size` at the start of each while-loop iteration to group nodes by depth level
- LeetCode 199 | Binary Tree Right Side View | Medium | https://leetcode.com/problems/binary-tree-right-side-view/ | Captures the last node of each level during a level-order walk
- LeetCode 103 | Binary Tree Zigzag Level Order Traversal | Medium | https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/ | Alternates left-to-right and right-to-left collection on each level
- LeetCode 116 | Populating Next Right Pointers in Each Node | Medium | https://leetcode.com/problems/populating-next-right-pointers-in-each-node/ | Connects level-order neighbours in O(1) extra space by walking already-linked parent pointers
- LeetCode 662 | Maximum Width of Binary Tree | Medium | https://leetcode.com/problems/maximum-width-of-binary-tree/ | Assigns heap-style indices `2*i` and `2*i + 1` during level-order BFS to measure level width
interview-questions:
- Why does a level-order walk use a **Queue** (FIFO) instead of a **Stack** (LIFO)?
- How does capturing `val levelSize = queue.size` before the inner `for` loop let you know exactly when one level ends and the next begins?
- For a balanced binary tree of n nodes, what is the maximum number of nodes held in the queue at one time during level-order traversal?Compare that memory to DFS on a skewed tree vs a wide balanced tree.
- If you want to find the shallowest leaf in a huge tree (`Minimum Depth of Binary Tree`), why is level-order BFS often much faster than DFS?

## dsa/bst/bst-search-and-insert
problems:
- LeetCode 700 | Search in a Binary Search Tree | Easy | https://leetcode.com/problems/search-in-a-binary-search-tree/ | Halves the search space at each node by comparing `target` with `node.val`
- HackerRank | Binary Search Tree : Insertion | Easy | https://www.hackerrank.com/challenges/binary-search-tree-insertion/problem | [Data Structures > Trees] Walks down to the correct null child link and attaches the new value
- LeetCode 701 | Insert into a Binary Search Tree | Medium | https://leetcode.com/problems/insert-into-a-binary-search-tree/ | Recursive and iterative BST insertion preserving the BST ordering invariant
- LeetCode 98 | Validate Binary Search Tree | Medium | https://leetcode.com/problems/validate-binary-search-tree/ | Enforces that every node inside a subtree falls strictly within `(minBound, maxBound)`, not just its immediate parent
- LeetCode 235 | Lowest Common Ancestor of a Binary Search Tree | Medium | https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/ | Uses the BST ordering property to walk straight to the split point in O(h) time and O(1) space
- LeetCode 450 | Delete Node in a BST | Medium | https://leetcode.com/problems/delete-node-in-a-bst/ | Handles all three deletion cases: leaf, one child, and two children (replacing with inorder successor)
interview-questions:
- Why is checking `node.left.val < node.val && node.right.val > node.val` at every node NOT enough to prove a binary tree is a valid BST?
- What does an Inorder traversal of a valid Binary Search Tree produce, and how does that help solve `Kth Smallest Element in a BST`?
- How do you delete a BST node that has **two** children without violating the BST invariant?
- When would you choose a `TreeMap` / `TreeSet` in Kotlin over a `HashMap` / `HashSet`?

## dsa/bst/bst-shape-and-balance
problems:
- LeetCode 108 | Convert Sorted Array to Binary Search Tree | Easy | https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/ | Picks the middle element as root recursively to build a height-balanced BST of height O(log n)
- LeetCode 110 | Balanced Binary Tree | Easy | https://leetcode.com/problems/balanced-binary-tree/ | Checks in O(n) bottom-up time whether every node's left and right subtrees differ in height by at most 1
- LeetCode 109 | Convert Sorted List to Binary Search Tree | Medium | https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/ | Builds a balanced BST from a sorted linked list in O(n) time
- LeetCode 1382 | Balance a Binary Search Tree | Medium | https://leetcode.com/problems/balance-a-binary-search-tree/ | Flattens a skewed BST via inorder traversal and rebuilds it into a balanced tree
- HackerRank | Self Balancing Tree | Medium | https://www.hackerrank.com/challenges/self-balancing-tree/problem | [Data Structures > Balanced Trees] Implements left and right AVL tree rotations to restore balance after insertion
interview-questions:
- What shape does a plain BST take if you insert `[1, 2, 3, 4, 5, 6, 7]` in ascending order, and what happens to the time complexity of `search()`?
- How does a tree rotation (left rotation / right rotation) change the height of subtrees while preserving the exact Inorder sequence of keys?
- What kind of self-balancing BST does Java/Kotlin's `TreeMap` use under the hood, and what height guarantee does it maintain?
- Why does Java 8+'s `HashMap` convert a bucket's linked list into a Red-Black Tree when a single bucket exceeds 8 colliding nodes?

## dsa/heaps/priority-queue-idea
problems:
- LeetCode 1046 | Last Stone Weight | Easy | https://leetcode.com/problems/last-stone-weight/ | Repeatedly pulls the two heaviest stones from a Max-Heap and pushes the remainder back
- LeetCode 703 | Kth Largest Element in a Stream | Easy | https://leetcode.com/problems/kth-largest-element-in-a-stream/ | Maintains a Min-Heap of size k so the k-th largest item is always at `peek()` in O(1)
- HackerRank | Jesse and Cookies | Easy | https://www.hackerrank.com/challenges/jesse-and-cookies/problem | [Data Structures > Heap] Repeatedly combines the two least sweet cookies using a Min-Heap
- LeetCode 347 | Top K Frequent Elements | Medium | https://leetcode.com/problems/top-k-frequent-elements/ | Uses a size-k Min-Heap over frequency entries to beat O(n log n) full sorting in O(n log k)
- LeetCode 621 | Task Scheduler | Medium | https://leetcode.com/problems/task-scheduler/ | Schedules the most frequent remaining tasks first using a Max-Heap and a cooldown queue
- LeetCode 295 | Find Median from Data Stream | Hard | https://leetcode.com/problems/find-median-from-data-stream/ | Balances a Max-Heap (lower half) and a Min-Heap (upper half) for O(log n) insertion and O(1) median lookup
interview-questions:
- Why do you use a **Min-Heap** of size k (instead of a Max-Heap) to track the **k largest** elements in a stream?
- What are the time complexities of `peek()`, ` offer()` / `add()`, and `poll()` on a `PriorityQueue` of size n?
- By default, is Java/Kotlin's `PriorityQueue` a Min-Heap or a Max-Heap, and how do you create the opposite one?
- Why does iterating over a `PriorityQueue` with a `for` loop NOT yield elements in sorted order?

## dsa/heaps/how-a-heap-works
problems:
- HackerRank | QHEAP1 | Easy | https://www.hackerrank.com/challenges/qheap1/problem | [Data Structures > Heap] Implements insert, delete, and print-minimum operations on a binary heap
- LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Builds a binary heap in-place (`siftDown`) to extract the k-th largest without library sorting
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implements in-place Heap Sort (`heapify` in O(n) + repeated swap-root-to-end and `siftDown`) in O(1) auxiliary space
- LeetCode 378 | Kth Smallest Element in a Sorted Matrix | Medium | https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/ | Pushes and sifts `(value, row, col)` tuples in a min-heap of size at most n
- HackerRank | Find the Running Median | Hard | https://www.hackerrank.com/challenges/find-the-running-median/problem | [Data Structures > Heap] Maintains two array-backed heaps with rebalancing after every stream element
interview-questions:
- If a binary heap is stored in a 0-indexed array, what are the exact array indices of the parent, left child, and right child of node `i`?
- Why can a binary heap be stored in a flat array with zero wasted slots and no node objects, whereas a general binary search tree cannot?
- Walk through `siftUp` (bubble up) on `insert()` and `siftDown` (bubble down) on `poll()`. Why do we swap the root with the last element before sifting down on `poll()`?
- Why does building a heap bottom-up from an unsorted array (`heapify`) take O(n) time instead of O(n log n)?

## dsa/graphs/what-is-a-graph
problems:
- LeetCode 1791 | Find Center of Star Graph | Easy | https://leetcode.com/problems/find-center-of-star-graph/ | Introduces vertices, edges, and vertex degree in an undirected graph
- LeetCode 997 | Find the Town Judge | Easy | https://leetcode.com/problems/find-the-town-judge/ | Distinguishes in-degree from out-degree in a directed graph
- LeetCode 1971 | Find if Path Exists in Graph | Easy | https://leetcode.com/problems/find-if-path-exists-in-graph/ | Checks reachability between two vertices in an undirected graph
- LeetCode 1557 | Minimum Number of Vertices to Reach All Nodes | Medium | https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/ | Identifies source nodes with in-degree 0 in a Directed Acyclic Graph (DAG)
- LeetCode 785 | Is Graph Bipartite? | Medium | https://leetcode.com/problems/is-graph-bipartite/ | Two-colors graph vertices to detect odd-length cycles
interview-questions:
- What is the difference between a directed and an undirected graph, a weighted and an unweighted graph, and a cyclic and an acyclic graph?
- How is a Tree a special case of a Graph, and how is a Linked List a special case of a Tree?
- Give three examples of real graphs in an Android project (for example: Gradle module dependencies, Dagger/Hilt dependency graph, Navigation graph).
- In a directed graph, what do **in-degree** and **out-degree** mean, and what does the sum of all in-degrees equal?

## dsa/graphs/storing-a-graph
problems:
- LeetCode 997 | Find the Town Judge | Easy | https://leetcode.com/problems/find-the-town-judge/ | Converts an edge list `[[a, b], ...]` into degree arrays
- LeetCode 547 | Number of Provinces | Medium | https://leetcode.com/problems/number-of-provinces/ | Traverses a graph given as a V x V **adjacency matrix** (`isConnected[i][j]`)
- LeetCode 841 | Keys and Rooms | Medium | https://leetcode.com/problems/keys-and-rooms/ | Traverses a graph given directly as an **adjacency list** (`List<List<Int>>`)
- LeetCode 133 | Clone Graph | Medium | https://leetcode.com/problems/clone-graph/ | Traverses and deep-copies an object-pointer graph representation using a visited map
- LeetCode 200 | Number of Islands | Medium | https://leetcode.com/problems/number-of-islands/ | Treats a 2D character grid as an **implicit graph** where each cell's neighbours are `(r±1, c)` and `(r, c±1)`
interview-questions:
- Compare an **Adjacency Matrix** (`Array<BooleanArray>`) and an **Adjacency List** (`Array<MutableList<Int>>`) in space complexity, checking if edge `(u, v)` exists, and iterating over all neighbours of `u`.
- What is a **sparse graph** vs a **dense graph**, and why would an adjacency matrix for a road network of 1,000,000 intersections blow up memory?
- When given a 2D grid problem (like `Number of Islands`), why shouldn't you build an explicit adjacency list first?
- How do you store edge weights in an adjacency list in Kotlin without boxing overhead?

## dsa/graphs/breadth-first-search
problems:
- LeetCode 733 | Flood Fill | Easy | https://leetcode.com/problems/flood-fill/ | Warm-up queue-based connected region fill on a 2D grid
- HackerRank | Breadth First Search: Shortest Reach | Medium | https://www.hackerrank.com/challenges/bfsshortreach/problem | [Algorithms > Graph Theory] Computes shortest hop distances from a start node in an unweighted graph
- LeetCode 994 | Rotting Oranges | Medium | https://leetcode.com/problems/rotting-oranges/ | Multi-source BFS starting from all initially rotten oranges simultaneously to count minimum minutes
- LeetCode 1091 | Shortest Path in Binary Matrix | Medium | https://leetcode.com/problems/shortest-path-in-binary-matrix/ | 8-directional grid BFS finding the fewest steps from top-left to bottom-right
- LeetCode 752 | Open the Lock | Medium | https://leetcode.com/problems/open-the-lock/ | State-space BFS where each 4-digit lock combination is a node and a wheel turn is an edge
- LeetCode 127 | Word Ladder | Hard | https://leetcode.com/problems/word-ladder/ | Unweighted shortest transformation sequence between words using BFS
interview-questions:
- Why does Breadth-First Search guarantee the **shortest path** (fewest edges) in an unweighted graph, whereas Depth-First Search does not?
- Why must you mark a node as `visited` **at the moment you enqueue it** rather than when you dequeue it? What bug happens on a grid if you mark it on dequeue?
- How does **Multi-Source BFS** work (like in `Rotting Oranges` or `01 Matrix`), and why does seeding the queue with all sources at step 0 take O(V + E) time?
- What is **Bidirectional BFS**, and why does searching simultaneously from `start` and `target` drastically cut the search space in `Word Ladder`?

## dsa/graphs/depth-first-search
problems:
- LeetCode 200 | Number of Islands | Medium | https://leetcode.com/problems/number-of-islands/ | Uses DFS to sink each connected component of land in O(R * C)
- LeetCode 695 | Max Area of Island | Medium | https://leetcode.com/problems/max-area-of-island/ | Returns subtree component counts recursively from a grid DFS
- LeetCode 797 | All Paths From Source to Target | Medium | https://leetcode.com/problems/all-paths-from-source-to-target/ | Explores every complete path from node 0 to node n - 1 in a DAG using DFS
- HackerRank | Roads and Libraries | Medium | https://www.hackerrank.com/challenges/torque-and-development/problem | [Algorithms > Graph Theory] Counts connected component sizes with DFS to minimize infrastructure cost
- LeetCode 130 | Surrounded Regions | Medium | https://leetcode.com/problems/surrounded-regions/ | Runs boundary-seeded DFS from the edges inward to mark regions that can escape capture
- LeetCode 329 | Longest Increasing Path in a Matrix | Hard | https://leetcode.com/problems/longest-increasing-path-in-a-matrix/ | Combines DFS path exploration with memoization on a directed grid graph
interview-questions:
- What is the time and space complexity of Depth-First Search on a graph with V vertices and E edges stored as an adjacency list?
- Why do we need 3 visitation states (`UNVISITED = 0`, `VISITING = 1` on current stack, `VISITED = 2` completed) to detect a cycle in a **directed** graph, whereas 2 states (`visited` boolean) suffice in an **undirected** graph (ignoring the immediate parent)?
- On a 1000 x 1000 grid in Android/JVM, why might a recursive DFS throw `StackOverflowError` while BFS or iterative DFS succeeds?
- When enumerating all paths (`All Paths From Source to Target`), why do you remove a node from the current path upon returning from the recursive DFS call, whereas in `Number of Islands` you leave cells permanently marked visited?

## dsa/graphs/dependency-order
problems:
- LeetCode 207 | Course Schedule | Medium | https://leetcode.com/problems/course-schedule/ | Detects whether circular prerequisites make a valid dependency order impossible
- LeetCode 210 | Course Schedule II | Medium | https://leetcode.com/problems/course-schedule-ii/ | Outputs a valid topological ordering of tasks using Kahn's in-degree BFS or postorder DFS
- LeetCode 802 | Find Eventual Safe States | Medium | https://leetcode.com/problems/find-eventual-safe-states/ | Identifies nodes that never lead into a dependency cycle via reverse-edge Kahn's algorithm or 3-color DFS
- LeetCode 2115 | Find All Possible Recipes from Given Supplies | Medium | https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/ | Models recipes and ingredients as a dependency graph resolved by Kahn's topological sort
- LeetCode 310 | Minimum Height Trees | Medium | https://leetcode.com/problems/minimum-height-trees/ | Repeatedly peels degree-1 leaf layers inward (topological leaf trimming) to find the graph centroids
- LeetCode 269 | Alien Dictionary | Hard | https://leetcode.com/problems/alien-dictionary/ | Extracts character ordering constraints from adjacent words and runs topological sort to deduce the alphabet
interview-questions:
- Walk through **Kahn's Algorithm** (in-degree + queue) for topological sort. How does it detect that the graph contains a cycle?
- Why does topological sort only exist for **Directed Acyclic Graphs (DAGs)**?
- How does Gradle determine which Android modules can be compiled in parallel and when a circular module dependency error must be thrown?
- In DFS-based topological sort, why do you push a node onto the result list in **postorder** (after visiting all its dependents) and then reverse the list?

## dsa/tries/what-is-a-trie
problems:
- LeetCode 14 | Longest Common Prefix | Easy | https://leetcode.com/problems/longest-common-prefix/ | Introduces shared character prefixes across a word list
- LeetCode 208 | Implement Trie (Prefix Tree) | Medium | https://leetcode.com/problems/implement-trie-prefix-tree/ | Implements `insert`, `search`, and `startsWith` from scratch using `TrieNode` children and `isEndOfWord`
- HackerRank | Contacts | Medium | https://www.hackerrank.com/challenges/contacts/problem | [Data Structures > Trie] Stores a subtree word count at each TrieNode to answer prefix-count queries in O(L) time
- LeetCode 211 | Design Add and Search Words Data Structure | Medium | https://leetcode.com/problems/design-add-and-search-words-data-structure/ | Combines Trie lookup with DFS branching when a wildcard `.` character appears
- LeetCode 1268 | Search Suggestions System | Medium | https://leetcode.com/problems/search-suggestions-system/ | Models an autocomplete search box returning the top 3 lexicographical matches after each typed character
- LeetCode 212 | Word Search II | Hard | https://leetcode.com/problems/word-search-ii/ | Uses a Trie of target words to prune grid backtracking simultaneously across thousands of words
interview-questions:
- Why does a Trie find whether any word starts with a prefix of length L in O(L) time independent of whether the dictionary has 100 or 10,000,000 words?
- Why is the `isEndOfWord` boolean flag necessary on a `TrieNode`? What goes wrong if you insert `"app"` and `"apple"` without it?
- Compare storing children in a `TrieNode` as `Array<TrieNode?>(26)` versus `HashMap<Char, TrieNode>`: when is each better?
- Why is a `HashSet<String>` usually better for exact word lookup (`contains("apple")`), whereas a `Trie` is vastly better for autocomplete (`startsWith("ap")`) and spell-checking?

## dsa/patterns/two-pointers
problems:
- LeetCode 125 | Valid Palindrome | Easy | https://leetcode.com/problems/valid-palindrome/ | Opposite-end pointers converging inward while skipping non-alphanumeric characters in O(1) space
- LeetCode 283 | Move Zeroes | Easy | https://leetcode.com/problems/move-zeroes/ | Same-direction reader and writer pointers compacting non-zero elements in place
- LeetCode 167 | Two Sum II - Input Array Is Sorted | Medium | https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/ | Eliminates the O(n) HashMap by walking `left` and `right` inward on a sorted array in O(1) space
- LeetCode 11 | Container With Most Water | Medium | https://leetcode.com/problems/container-with-most-water/ | Greedily advances the shorter boundary pointer inward to replace an O(n^2) pair search with O(n)
- LeetCode 15 | 3Sum | Medium | https://leetcode.com/problems/3sum/ | Fixes one index and sweeps two pointers across the sorted remainder while skipping duplicates
- LeetCode 42 | Trapping Rain Water | Hard | https://leetcode.com/problems/trapping-rain-water/ | Two converging pointers tracking `leftMax` and `rightMax` to compute trapped water in O(n) time and O(1) space
interview-questions:
- In `Two Sum II` (sorted array), prove why moving `right--` when `nums[left] + nums[right] > target` can never accidentally skip the valid pair.
- What are the two main flavors of the Two Pointers pattern (converging opposite-ends vs same-direction fast/slow reader/writer), and what problems does each solve?
- In `Container With Most Water`, why is it always safe to move the pointer at the **shorter** line inward?
- When does a Two Pointers approach require sorting the array first, and why wouldn't Two Pointers + Sort work directly on the original `1. Two Sum` if it asks for original indices (unless you pair values with their original indices)?

## dsa/patterns/sliding-window
problems:
- LeetCode 643 | Maximum Average Subarray I | Easy | https://leetcode.com/problems/maximum-average-subarray-i/ | Fixed-size window of length k: add incoming right element and subtract outgoing left element in O(1)
- LeetCode 3 | Longest Substring Without Repeating Characters | Medium | https://leetcode.com/problems/longest-substring-without-repeating-characters/ | Variable-size window expanding `right` and shrinking `left` whenever a duplicate enters
- LeetCode 209 | Minimum Size Subarray Sum | Medium | https://leetcode.com/problems/minimum-size-subarray-sum/ | Shrinks the window from the left as long as the sum is at least `target` to find the shortest valid span
- LeetCode 424 | Longest Repeating Character Replacement | Medium | https://leetcode.com/problems/longest-repeating-character-replacement/ | Tracks the most frequent character inside the window so `windowSize - maxCount <= k`
- LeetCode 567 | Permutation in String | Medium | https://leetcode.com/problems/permutation-in-string/ | Fixed-length sliding character frequency window matching a target anagram
- LeetCode 76 | Minimum Window Substring | Hard | https://leetcode.com/problems/minimum-window-substring/ | Expands until all required characters are satisfied, then contracts `left` to find the minimum window in O(n)
interview-questions:
- What is the difference between a **fixed-size** sliding window and a **variable-size** sliding window?
- Why does the variable-size sliding window for `Minimum Size Subarray Sum` fail if the array can contain **negative** numbers?
- Explain clearly why a variable sliding window with `for (right in nums.indices) { while (...) { left++ } }` executes at most `2n` pointer movements total, not `n^2`.
- In `Longest Substring Without Repeating Characters`, how can storing each character's last seen index in a map let `left` jump directly past the duplicate instead of stepping one by one?

## dsa/patterns/prefix-sums
problems:
- LeetCode 1480 | Running Sum of 1d Array | Easy | https://leetcode.com/problems/running-sum-of-1d-array/ | Builds the foundational prefix sum array `prefix[i] = prefix[i - 1] + nums[i]`
- LeetCode 303 | Range Sum Query - Immutable | Easy | https://leetcode.com/problems/range-sum-query-immutable/ | Precomputes prefix sums in O(n) so any `sumRange(left, right)` query runs in O(1)
- LeetCode 724 | Find Pivot Index | Easy | https://leetcode.com/problems/find-pivot-index/ | Uses total sum and running prefix sum to compare left and right subarray totals in O(1) space
- LeetCode 560 | Subarray Sum Equals K | Medium | https://leetcode.com/problems/subarray-sum-equals-k/ | Combines prefix sums with a HashMap (`prefixSum -> count`) to handle negative numbers in O(n)
- LeetCode 304 | Range Sum Query 2D - Immutable | Medium | https://leetcode.com/problems/range-sum-query-2d-immutable/ | 2D inclusion-exclusion prefix sums answering any sub-rectangle sum in O(1)
- HackerRank | Array Manipulation | Hard | https://www.hackerrank.com/challenges/crush/problem | [Data Structures > Arrays] Uses the inverse of prefix sums (a difference array: `+k` at `a`, `-k` at `b+1`) to apply m range updates in O(n + m)
interview-questions:
- Why do we usually allocate a prefix sum array of size `n + 1` with `prefix[0] = 0` so that `sum(left..right) = prefix[right + 1] - prefix[left]`?
- Why does `Subarray Sum Equals K` (LeetCode 560) use Prefix Sums + HashMap instead of a Sliding Window?
- How does a **Difference Array** allow you to add `v` to every element in `arr[L..R]` across 100,000 queries in O(1) per query before taking a final prefix sum?
- In 2D prefix sums, write the inclusion-exclusion formula for the sum of rectangle `(r1, c1)` to `(r2, c2)` and explain why the top-left corner is added back.

## dsa/patterns/choose-the-right-structure
problems:
- LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Combines an `ArrayList` (for O(1) random index access) with a `HashMap` (for O(1) index lookup on delete)
- LeetCode 146 | LRU Cache | Medium | https://leetcode.com/problems/lru-cache/ | Pairs a `HashMap` (for O(1) key lookup) with a Doubly Linked List (for O(1) recency reordering)
- LeetCode 981 | Time Based Key-Value Store | Medium | https://leetcode.com/problems/time-based-key-value-store/ | Combines a `HashMap<String, ArrayList<TimedValue>>` with Binary Search on monotonically increasing timestamps
- LeetCode 355 | Design Twitter | Medium | https://leetcode.com/problems/design-twitter/ | Combines a `HashMap` of follower `HashSet`s, per-user tweet lists, and a `PriorityQueue` k-way merge for the news feed
- LeetCode 295 | Find Median from Data Stream | Hard | https://leetcode.com/problems/find-median-from-data-stream/ | Chooses two balanced heaps over a sorted list to get both O(log n) insertion and O(1) median retrieval
- LeetCode 432 | All O`one Data Structure | Hard | https://leetcode.com/problems/all-oone-data-structure/ | Pairs a `HashMap` with a doubly linked list of count-buckets to maintain `inc`, `dec`, `getMaxKey`, and `getMinKey` all in O(1)
interview-questions:
- If an interviewer asks you to design a data structure supporting operations X, Y, and Z in specific time complexities, how do you systematically combine two structures (such as `HashMap` + `ArrayList` or `HashMap` + `LinkedList`) to cover each other's weaknesses?
- Which data structure would you choose for an Android in-memory image cache that must evict the least recently used bitmap when total byte size exceeds 32 MB?
- You need to store sensor readings arriving in timestamp order and frequently query "what was the latest reading at or before time T for sensor ID S?". Which combination of data structures gives the best time and memory complexity?
- Compare `HashMap`, `TreeMap`, `LinkedHashMap`, and `ArrayDeque`: what is the unique superpower of each one?

## Self-check
- LeetCode 1342 | Number of Steps to Reduce a Number to Zero | Verify exact problem number (1342) and slug (`number-of-steps-to-reduce-a-number-to-zero`).
- LeetCode 1089 | Duplicate Zeros | Verify exact problem number (1089) and Easy difficulty classification.
- LeetCode 1929 | Concatenation of Array | Verify exact problem number (1929).
- LeetCode 1636 | Sort Array by Increasing Frequency | Verify exact problem number (1636).
- LeetCode 1366 | Rank Teams by Votes | Verify exact problem number (1366) and exact title casing.
- LeetCode 1122 | Relative Sort Array | Verify exact problem number (1122).
- LeetCode 2236 | Root Equals Sum of Children | Verify exact problem number (2236).
- LeetCode 1971 | Find if Path Exists in Graph | Verify exact problem number (1971).
- LeetCode 1557 | Minimum Number of Vertices to Reach All Nodes | Verify exact problem number (1557).
- LeetCode 2115 | Find All Possible Recipes from Given Supplies | Verify exact problem number (2115).