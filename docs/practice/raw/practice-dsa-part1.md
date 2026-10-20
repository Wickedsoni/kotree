# practice: dsa

## dsa/complexity/counting-steps
problems:
- LeetCode 1480 | Running Sum of 1d Array | Easy | https://leetcode.com/problems/running-sum-of-1d-array/ | Count exact loop iterations and assignments for a single pass over n elements
- LeetCode 1342 | Number of Steps to Reduce a Number to Zero | Easy | https://leetcode.com/problems/number-of-steps-to-reduce-a-number-to-zero/ | Directly simulate and count operations as input scales by halving and subtracting
- LeetCode 1512 | Number of Good Pairs | Easy | https://leetcode.com/problems/number-of-good-pairs/ | Compare the exact n(n-1)/2 step count of nested loops against a single-pass frequency count
- HackerRank (Algorithms > Implementation) | Find Digits | Easy | https://www.hackerrank.com/challenges/find-digits/problem | Count division steps proportional to the number of base-10 digits rather than value size
- LeetCode 204 | Count Primes | Medium | https://leetcode.com/problems/count-primes/ | Observe why trial division times out at large n while Sieve of Eratosthenes reduces total steps
interview-questions:
- If an algorithm takes 1,000 steps for n = 10 and 1,000,000 steps for n = 100, how many steps do you expect for n = 1,000?
- Why does counting inner-loop executions give a better runtime estimate than counting lines of source code?
- How many times does `i = i * 2` execute before exceeding n = 1,000,000?
- On Android's 16ms UI frame budget, roughly how many basic loop iterations can run on the main thread before dropping frames?

## dsa/complexity/big-o-notation
problems:
- LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Contrast O(log n) halving with O(n) linear scan as n grows
- LeetCode 169 | Majority Element | Easy | https://leetcode.com/problems/majority-element/ | Compare O(n log n) sorting, O(n) hash map space, and O(n) time / O(1) space Boyer-Moore voting
- LeetCode 53 | Maximum Subarray | Medium | https://leetcode.com/problems/maximum-subarray/ | Reduce O(n^2) subarray checks to O(n) using Kadane's algorithm
- LeetCode 50 | Pow(x, n) | Medium | https://leetcode.com/problems/powx-n/ | Turn O(n) multiplications into O(log n) via binary exponentiation
- LeetCode 4 | Median of Two Sorted Arrays | Hard | https://leetcode.com/problems/median-of-two-sorted-arrays/ | Enforce a strict O(log(m + n)) time bound where an O(m + n) merge is too slow
interview-questions:
- Why does Big-O drop constants and lower-order terms like the 50 in O(2n + 50)?
- When can an O(n^2) algorithm outperform an O(n log n) algorithm in real production code?
- Is O(1) guaranteed to be instant, or does it only mean the cost does not grow with input size?
- What is the Big-O time and space complexity of copying an array of size n?

## dsa/complexity/reading-complexity-in-code
problems:
- LeetCode 283 | Move Zeroes | Easy | https://leetcode.com/problems/move-zeroes/ | Spot how repeatedly shifting elements inside a loop hides O(n^2) work and fix it in O(n)
- LeetCode 14 | Longest Common Prefix | Easy | https://leetcode.com/problems/longest-common-prefix/ | Analyze two nested loops where the bound depends on both string count and string length
- LeetCode 209 | Minimum Size Subarray Sum | Medium | https://leetcode.com/problems/minimum-size-subarray-sum/ | Recognize why a `while` loop inside a `for` loop is still O(n) when both pointers only move forward
- LeetCode 15 | 3Sum | Medium | https://leetcode.com/problems/3sum/ | Combine sorting O(n log n) with a nested two-pointer scan to achieve O(n^2) instead of O(n^3)
- LeetCode 84 | Largest Rectangle in Histogram | Hard | https://leetcode.com/problems/largest-rectangle-in-histogram/ | Prove amortized O(n) complexity when each element is pushed and popped at most once
interview-questions:
- Why isn't every `while` loop nested inside a `for` loop automatically O(n^2)?
- What hidden time complexity is triggered if you call `list.contains(x)` inside a `for` loop over `list` in Kotlin?
- What is the time complexity of an outer loop running n times and an inner loop running `n, n/2, n/4, ..., 1` times?
- How do you analyze the time complexity of a chain of Kotlin collection operators like `.filter { ... }.map { ... }.sortedBy { ... }`?

## dsa/complexity/time-vs-space
problems:
- LeetCode 217 | Contains Duplicate | Easy | https://leetcode.com/problems/contains-duplicate/ | Trade O(n) extra memory in a HashSet to drop time from O(n log n) to O(n)
- LeetCode 1 | Two Sum | Easy | https://leetcode.com/problems/two-sum/ | Spend O(n) space on a complement map to avoid an O(n^2) pair search
- LeetCode 136 | Single Number | Easy | https://leetcode.com/problems/single-number/ | Reclaim O(1) space from an O(n) hash map solution by using XOR properties
- LeetCode 238 | Product of Array Except Self | Medium | https://leetcode.com/problems/product-of-array-except-self/ | Eliminate O(n) prefix/suffix arrays by accumulating results directly in the output array
- LeetCode 287 | Find the Duplicate Number | Medium | https://leetcode.com/problems/find-the-duplicate-number/ | Solve in O(n) time and strict O(1) extra space using Floyd's cycle detection
- LeetCode 41 | First Missing Positive | Hard | https://leetcode.com/problems/first-missing-positive/ | Reuse the input array as an in-place hash table to keep auxiliary space at O(1)
interview-questions:
- On a memory-constrained Android device, when would you choose an O(n log n) in-place sort over an O(n) HashMap solution?
- Does modifying the input array in-place count as O(1) space in an interview, and what are the side effects for the caller?
- How does precomputing a lookup table trade startup memory for faster frame-by-frame queries?
- Why does recursion count toward space complexity even if you allocate no arrays or collections?

## dsa/arrays/arrays-in-memory
problems:
- LeetCode 1920 | Build Array from Permutation | Easy | https://leetcode.com/problems/build-array-from-permutation/ | Practice direct O(1) random access via index arithmetic `nums[nums[i]]`
- LeetCode 66 | Plus One | Easy | https://leetcode.com/problems/plus-one/ | Traverse contiguous memory backward and handle full-array reallocation on carry overflow
- LeetCode 189 | Rotate Array | Medium | https://leetcode.com/problems/rotate-array/ | Use three in-place array reversals to rotate contiguous memory in O(1) extra space
- LeetCode 48 | Rotate Image | Medium | https://leetcode.com/problems/rotate-image/ | Map 2D row-and-column coordinates in-place by transposing and reversing rows
- LeetCode 54 | Spiral Matrix | Medium | https://leetcode.com/problems/spiral-matrix/ | Navigate 2D array boundaries cleanly without out-of-bounds reads
interview-questions:
- How does the CPU compute the memory address of `arr[500]` in a single step?
- In Kotlin, what is the memory layout difference between `IntArray` and `Array<Int>`?
- Why does iterating through a 2D array row-by-row run faster than column-by-column on modern CPU caches?
- Why must all elements in a primitive array have the exact same byte size for O(1) indexing to work?

## dsa/arrays/insert-and-delete-in-an-array
problems:
- LeetCode 27 | Remove Element | Easy | https://leetcode.com/problems/remove-element/ | Avoid O(n^2) repeated shifting by overwriting deleted slots in a single O(n) pass
- LeetCode 26 | Remove Duplicates from Sorted Array | Easy | https://leetcode.com/problems/remove-duplicates-from-sorted-array/ | Compact unique elements to the front of an array using a read/write pointer pair
- LeetCode 88 | Merge Sorted Array | Easy | https://leetcode.com/problems/merge-sorted-array/ | Write from the back of the array to insert elements without overwriting unread values
- HackerRank (Data Structures > Arrays) | Left Rotation | Easy | https://www.hackerrank.com/challenges/array-left-rotation/problem | See why shifting left one by one is O(n * d) while index-offsetting is O(n)
- LeetCode 80 | Remove Duplicates from Sorted Array II | Medium | https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/ | Generalize in-place array compaction when up to two duplicates are allowed
interview-questions:
- Why does `list.removeAt(0)` on an `ArrayList` of 100,000 items take O(n) time while `list.removeAt(list.lastIndex)` takes O(1)?
- How can you delete an element at index `i` from an unsorted `ArrayList` in O(1) time if order does not matter?
- If you need to remove half the items from a `MutableList` in Kotlin, why is `removeAll { ... }` much faster than calling `removeAt(i)` in a loop?
- How does `System.arraycopy` move a block of array elements faster than a manual `for` loop?

## dsa/arrays/how-arraylist-grows
problems:
- LeetCode 1929 | Concatenation of Array | Easy | https://leetcode.com/problems/concatenation-of-array/ | Allocate a new array of size 2n and copy existing elements over
- LeetCode 118 | Pascal's Triangle | Easy | https://leetcode.com/problems/pascals-triangle/ | Dynamically grow nested lists row by row with known capacities
- HackerRank (Data Structures > Arrays) | Dynamic Array | Easy | https://www.hackerrank.com/challenges/dynamic-array/problem | Manage a collection of dynamically growing sequences under append and lookup queries
- LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Combine a resizable `ArrayList` with a `HashMap` to get O(1) amortized append and swap-delete
- LeetCode 443 | String Compression | Medium | https://leetcode.com/problems/string-compression/ | Compare appending to a resizable buffer against compacting an existing array in-place
interview-questions:
- If an `ArrayList` doubles its backing array whenever it fills up, why is the average cost of `add()` still O(1) amortized?
- Why would growing a backing array by a fixed +10 slots each time degrade `n` appends to O(n^2) total time?
- When should you pass `initialCapacity` to `ArrayList(capacity)` in Kotlin?
- Does removing elements from a Java/Kotlin `ArrayList` automatically shrink the backing array in memory?

## dsa/strings/text-building-cost
problems:
- LeetCode 1108 | Defanging an IP Address | Easy | https://leetcode.com/problems/defanging-an-ip-address/ | Build a transformed string in a single O(n) pass using `StringBuilder`
- LeetCode 1768 | Merge Strings Alternately | Easy | https://leetcode.com/problems/merge-strings-alternately/ | Pre-size a character buffer and interleave two strings without intermediate allocations
- LeetCode 151 | Reverse Words in a String | Medium | https://leetcode.com/problems/reverse-words-in-a-string/ | Parse words and join them in reverse order with O(n) total string-building work
- LeetCode 6 | Zigzag Conversion | Medium | https://leetcode.com/problems/zigzag-conversion/ | Accumulate characters into row-specific `StringBuilder` buffers and merge once at the end
- LeetCode 394 | Decode String | Medium | https://leetcode.com/problems/decode-string/ | Repeatedly expand nested `[k[encoded]]` patterns without quadratic string copying
- LeetCode 68 | Text Justification | Hard | https://leetcode.com/problems/text-justification/ | Construct padded lines to exact width using precalculated spacing in O(n) time
interview-questions:
- Why does `result += char` inside a loop of length n allocate O(n^2) total characters in Kotlin/Java?
- How does `StringBuilder` avoid creating a new `String` object on every append?
- Why are `String` objects immutable on the JVM, and what benefits does immutability give to security and hashing?
- Under the hood, how does Kotlin compile a single string template like `"User $name has $count items"` versus concatenation inside a loop?

## dsa/strings/compare-text-by-characters
problems:
- LeetCode 242 | Valid Anagram | Easy | https://leetcode.com/problems/valid-anagram/ | Use a 26-slot `IntArray` frequency counter to compare two words in O(n) time and O(1) space
- LeetCode 383 | Ransom Note | Easy | https://leetcode.com/problems/ransom-note/ | Verify character availability by decrementing frequency counts in one pass
- LeetCode 387 | First Unique Character in a String | Easy | https://leetcode.com/problems/first-unique-character-in-a-string/ | Two-pass character frequency counting to find the first count of 1
- LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | Bucket strings by their character-count signature without sorting each word
- LeetCode 438 | Find All Anagrams in a String | Medium | https://leetcode.com/problems/find-all-anagrams-in-a-string/ | Maintain a sliding 26-character frequency window to match anagrams in O(n)
- LeetCode 76 | Minimum Window Substring | Hard | https://leetcode.com/problems/minimum-window-substring/ | Track satisfied character counts across a dynamic window in O(n) time
interview-questions:
- Why is counting characters in an `IntArray(26)` faster than sorting both strings in O(n log n)?
- When must you switch from a fixed `IntArray(26)` to a `HashMap<Char, Int>` when processing user text on Android?
- How does UTF-16 surrogate-pair representation affect `String.length` when a string contains emojis?
- How can a single 32-bit `Int` bitmask check whether two lowercase ASCII words share any common letters?

## dsa/linked-lists/what-is-a-linked-list
problems:
- LeetCode 1290 | Convert Binary Number in a Linked List to Integer | Easy | https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/ | Walk a singly linked list from `head` to `null` while accumulating state
- LeetCode 876 | Middle of the Linked List | Easy | https://leetcode.com/problems/middle-of-the-linked-list/ | Use slow and fast pointers to find the midpoint in one traversal without knowing length
- LeetCode 141 | Linked List Cycle | Easy | https://leetcode.com/problems/linked-list-cycle/ | Detect whether a chain of `next` pointers loops back on itself in O(1) space
- LeetCode 160 | Intersection of Two Linked Lists | Easy | https://leetcode.com/problems/intersection-of-two-linked-lists/ | Synchronize two pointer traversals to find where two node chains merge in memory
- LeetCode 142 | Linked List Cycle II | Medium | https://leetcode.com/problems/linked-list-cycle-ii/ | Prove and locate the exact node where a cycle begins using pointer speeds
- LeetCode 138 | Copy List with Random Pointer | Medium | https://leetcode.com/problems/copy-list-with-random-pointer/ | Deep-copy heap-allocated nodes that hold both sequential and arbitrary references
interview-questions:
- Why does finding the 500th element of a linked list take O(n) steps while an array takes O(1)?
- How much extra memory overhead does each node in a doubly linked list carry compared to a primitive array element on a 64-bit JVM?
- Why are linked list traversals often slower than array traversals even when both are O(n)?
- How does Floyd's tortoise-and-hare algorithm guarantee that the fast pointer won't just jump over the slow pointer without meeting?

## dsa/linked-lists/insert-and-remove-nodes
problems:
- LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Master rewiring `next` pointers iteratively and recursively without losing the rest of the list
- LeetCode 203 | Remove Linked List Elements | Easy | https://leetcode.com/problems/remove-linked-list-elements/ | Use a dummy sentinel node to handle deleting the head node cleanly
- LeetCode 237 | Delete Node in a Linked List | Medium | https://leetcode.com/problems/delete-node-in-a-linked-list/ | Bypass a node when you aren't given access to the `head` pointer
- LeetCode 19 | Remove Nth Node From End of List | Medium | https://leetcode.com/problems/remove-nth-node-from-end-of-list/ | Maintain a k-step gap between two pointers to unlink a target node in one pass
- LeetCode 24 | Swap Nodes in Pairs | Medium | https://leetcode.com/problems/swap-nodes-in-pairs/ | Re-link adjacent pairs of nodes carefully without creating a self-loop
- LeetCode 25 | Reverse Nodes in k-Group | Hard | https://leetcode.com/problems/reverse-nodes-in-k-group/ | Combine group counting, in-place sub-list reversal, and tail reconnection
interview-questions:
- What problem does a dummy (`sentinel`) head node solve when inserting or deleting in a singly linked list?
- In what exact order must you update pointers when inserting a new node between `prev` and `curr`?
- Why can you delete a node in a doubly linked list in O(1) given only a reference to that node, whereas a singly linked list normally needs the predecessor?
- What happens in garbage-collected languages like Kotlin if you unlink a node from a list, versus if a stagnant reference still points to it?

## dsa/linked-lists/linked-list-vs-arraylist
problems:
- LeetCode 707 | Design Linked List | Medium | https://leetcode.com/problems/design-linked-list/ | Implement `get`, `addAtHead`, `addAtTail`, `addAtIndex`, and `deleteAtIndex` to feel every cost directly
- LeetCode 2 | Add Two Numbers | Medium | https://leetcode.com/problems/add-two-numbers/ | Stream node-by-node creation where output length isn't fixed ahead of time
- LeetCode 143 | Reorder List | Medium | https://leetcode.com/problems/reorder-list/ | Experience why O(1)-space linked list interleaving requires finding the middle, reversing, and merging
- LeetCode 1472 | Design Browser History | Medium | https://leetcode.com/problems/design-browser-history/ | Compare a doubly linked list against a dynamic array with a cursor for `visit`, `back`, and `forward`
- LeetCode 146 | LRU Cache | Medium | https://leetcode.com/problems/lru-cache/ | See the textbook case where a doubly linked list + HashMap beats an `ArrayList` with O(1) middle removal
interview-questions:
- Why does Kotlin's standard library default `mutableListOf()` to `ArrayList` and almost never recommend `LinkedList`?
- If inserting in the middle of a `LinkedList` is O(1) once you are at the node, why is `linkedList.add(index, element)` still O(n)?
- In `LRU Cache`, why does a doubly linked list beat an `ArrayList` when promoting a recently accessed item to the front?
- Why did Kotlin introduce `ArrayDeque` instead of recommending `LinkedList` for queue and stack operations?

## dsa/stacks/what-is-a-stack
problems:
- LeetCode 682 | Baseball Game | Easy | https://leetcode.com/problems/baseball-game/ | Push scores and pop/peek previous entries to model undo and history operations
- LeetCode 1047 | Remove All Adjacent Duplicates In String | Easy | https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/ | Use the top of a stack to cancel adjacent matching characters in O(n)
- LeetCode 232 | Implement Queue using Stacks | Easy | https://leetcode.com/problems/implement-queue-using-stacks/ | Pour elements between two LIFO stacks to reverse order into FIFO in amortized O(1)
- LeetCode 155 | Min Stack | Medium | https://leetcode.com/problems/min-stack/ | Augment a stack with running minimums so `push`, `pop`, `top`, and `getMin` are all O(1)
- LeetCode 150 | Evaluate Reverse Polish Notation | Medium | https://leetcode.com/problems/evaluate-reverse-polish-notation/ | Evaluate postfix arithmetic expressions using an operand stack
- LeetCode 739 | Daily Temperatures | Medium | https://leetcode.com/problems/daily-temperatures/ | Maintain a monotonic decreasing stack to find the next warmer day in O(n)
interview-questions:
- How does the Android Activity back stack use LIFO ordering when a user presses the Back button?
- Why should you use `ArrayDeque` instead of `java.util.Stack` in Kotlin and Java?
- How can `Min Stack` support `getMin()` in O(1) time even after the minimum element is popped?
- What is a monotonic stack, and what kind of "next greater element" problems does it reduce from O(n^2) to O(n)?

## dsa/stacks/matching-brackets
problems:
- LeetCode 20 | Valid Parentheses | Easy | https://leetcode.com/problems/valid-parentheses/ | Push opening brackets and verify that every closing bracket matches the top of the stack
- LeetCode 1614 | Maximum Nesting Depth of the Parentheses | Easy | https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/ | Track current stack depth to measure maximum nesting level
- HackerRank (Data Structures > Stacks) | Balanced Brackets | Medium | https://www.hackerrank.com/challenges/balanced-brackets/problem | Handle edge cases like unmatched closing brackets and leftover open brackets at the end
- LeetCode 921 | Minimum Add to Make Parentheses Valid | Medium | https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/ | Count unmatched opening and closing brackets to repair an invalid string
- LeetCode 1249 | Minimum Remove to Make Valid Parentheses | Medium | https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/ | Push character indices onto a stack so you know exactly which invalid brackets to delete
- LeetCode 32 | Longest Valid Parentheses | Hard | https://leetcode.com/problems/longest-valid-parentheses/ | Use a stack of indices with a base sentinel to measure the longest balanced substring in O(n)
interview-questions:
- Why can't you validate nested brackets of three types `()`, `[]`, `{}` simply by counting +1 for open and -1 for close?
- For a single bracket type `()`, how can you check validity in O(1) space without allocating a stack?
- What two edge cases cause a naive bracket-matching solution to crash with `NoSuchElementException` or return `true` on `"((("`?
- Why is pushing the *index* of an opening bracket onto the stack often more useful than pushing the character itself?

## dsa/queues/what-is-a-queue
problems:
- LeetCode 225 | Implement Stack using Queues | Easy | https://leetcode.com/problems/implement-stack-using-queues/ | Rotate a FIFO queue on push or pop to simulate LIFO behavior
- LeetCode 2073 | Time Needed to Buy Tickets | Easy | https://leetcode.com/problems/time-needed-to-buy-tickets/ | Model round-robin FIFO service where people re-enter the back of the line
- LeetCode 622 | Design Circular Queue | Medium | https://leetcode.com/problems/design-circular-queue/ | Implement `head` and `tail` wrap-around indices on a fixed-size array with `(index + 1) % capacity`
- LeetCode 950 | Reveal Cards In Increasing Order | Medium | https://leetcode.com/problems/reveal-cards-in-increasing-order/ | Simulate deck-dealing rules backward using a queue of indices
- LeetCode 649 | Dota2 Senate | Medium | https://leetcode.com/problems/dota2-senate/ | Use two FIFO queues storing round indices to simulate cyclic turn-based elimination
interview-questions:
- How does a circular buffer (`ArrayDeque`) achieve O(1) `addLast` and `removeFirst` on a plain array without shifting elements?
- Where are FIFO queues used inside Android's `Looper` and `MessageQueue` architecture?
- When using `head` and `tail` indices in a circular array, how do you distinguish a completely full queue from a completely empty queue?
- What is the difference between a standard Queue, a Deque (double-ended queue), and a PriorityQueue?

## dsa/queues/keeping-the-last-n-items
problems:
- LeetCode 933 | Number of Recent Calls | Easy | https://leetcode.com/problems/number-of-recent-calls/ | Push new timestamps and evict anything older than `t - 3000` from the front of the queue
- LeetCode 346 | Moving Average from Data Stream | Easy | https://leetcode.com/problems/moving-average-from-data-stream/ | Maintain a fixed-capacity queue of the last k values and update a running sum in O(1)
- LeetCode 641 | Design Circular Deque | Medium | https://leetcode.com/problems/design-circular-deque/ | Support bounded insertions and evictions at both ends of a ring buffer
- LeetCode 362 | Design Hit Counter | Medium | https://leetcode.com/problems/design-hit-counter/ | Aggregate and evict timestamped hits over a rolling 300-second window
- LeetCode 239 | Sliding Window Maximum | Hard | https://leetcode.com/problems/sliding-window-maximum/ | Keep a monotonic deque of candidate indices to query the max of the last k items in O(1)
interview-questions:
- If you want to keep only the latest 100 log lines in an Android diagnostic buffer, why is a circular array better than a `MutableList`?
- How do you maintain the average of the last N stream values in O(1) time per item without summing the queue every time?
- In `Design Hit Counter`, what breaks if 10,000 requests arrive at the exact same millisecond and you store every timestamp individually?
- How does a monotonic deque keep track of the maximum in a sliding window when the old maximum ages out of the window?

## dsa/hashing/hashing-idea
problems:
- LeetCode 217 | Contains Duplicate | Easy | https://leetcode.com/problems/contains-duplicate/ | Replace O(n^2) pairwise checks with O(1) average-time `HashSet` lookups
- LeetCode 1 | Two Sum | Easy | https://leetcode.com/problems/two-sum/ | Map values to indices for instant complement lookup in a single pass
- LeetCode 349 | Intersection of Two Arrays | Easy | https://leetcode.com/problems/intersection-of-two-arrays/ | Convert one list into a set to filter another list in O(n + m) time
- HackerRank (Data Structures > Dictionaries and Hashmaps) | Frequency Queries | Medium | https://www.hackerrank.com/challenges/frequency-queries/problem | Maintain two synchronized hash maps (value-to-count and count-to-frequency) for O(1) queries
- LeetCode 128 | Longest Consecutive Sequence | Medium | https://leetcode.com/problems/longest-consecutive-sequence/ | Use a `HashSet` to start sequence scans only at sequence heads, reducing O(n log n) to O(n)
- LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Map each element to its array index so removal by value is O(1)
interview-questions:
- How does a hash table convert an arbitrary key like `"user_42"` into an array bucket index?
- What is the load factor of a `HashMap`, and why does Java/Kotlin resize the table when it reaches 0.75?
- Why is iterating over a `HashSet` unordered, and how does `LinkedHashSet` preserve insertion order?
- Why does resizing a `HashMap` require recomputing bucket positions for every existing entry?

## dsa/hashing/collisions
problems:
- LeetCode 705 | Design HashSet | Easy | https://leetcode.com/problems/design-hashset/ | Build a hash set from scratch using an array of buckets and separate chaining for collisions
- LeetCode 706 | Design HashMap | Easy | https://leetcode.com/problems/design-hashmap/ | Store `(key, value)` nodes in collision chains and handle key updates vs new insertions
- LeetCode 205 | Isomorphic Strings | Easy | https://leetcode.com/problems/isomorphic-strings/ | Prevent two distinct characters from colliding into the same mapped target character
- LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | intentionally design a canonical hash key so all anagrams collide into the same group
- LeetCode 535 | Encode and Decode TinyURL | Medium | https://leetcode.com/problems/encode-and-decode-tinyurl/ | Handle short-key generation and collision resolution in a bi-directional map
interview-questions:
- What is the difference between separate chaining and open addressing (linear probing) when two keys land in the same bucket?
- Why is a `HashMap` lookup O(1) on average but O(n) in the worst case, and how did Java 8 mitigate this with balanced trees (`TreeNode`)?
- In open addressing with linear probing, why can't you simply set a bucket to `null` when deleting an entry?
- What is a hash flooding Denial-of-Service attack, and how can attackers exploit predictable hash functions?

## dsa/hashing/hashcode-and-equals-contract
problems:
- LeetCode 706 | Design HashMap | Easy | https://leetcode.com/problems/design-hashmap/ | See firsthand how bucket index uses `hashCode()` while bucket chain traversal uses `equals()`
- LeetCode 219 | Contains Duplicate II | Easy | https://leetcode.com/problems/contains-duplicate-ii/ | Rely on value equality inside a map to track the most recent index of an equal key
- LeetCode 290 | Word Pattern | Easy | https://leetcode.com/problems/word-pattern/ | Distinguish reference identity from structural `equals()` when mapping `Char` and `String` keys
- LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | Discover why `IntArray` fails as a `HashMap` key in Kotlin/Java (reference `hashCode`) while `String` or `List<Int>` works
- LeetCode 149 | Max Points on a Line | Hard | https://leetcode.com/problems/max-points-on-a-line/ | Represent reduced slope fractions `(dy, dx)` as a custom key with proper `equals` and `hashCode` instead of lossy `Double`
interview-questions:
- What are the exact rules of the `equals()` and `hashCode()` contract on the JVM?
- What happens when you put an object into a `HashSet`, mutate one of the fields used in its `hashCode()`, and then call `set.contains(obj)`?
- Why does using a `ByteArray` or `IntArray` inside a Kotlin `data class` break value equality in a `HashMap`, and how do you fix it?
- If two objects have the same `hashCode()`, must `equals()` return `true`? What if `equals()` returns `true`—must their `hashCode()` be identical?

## dsa/recursion/what-is-recursion
problems:
- LeetCode 344 | Reverse String | Easy | https://leetcode.com/problems/reverse-string/ | Identify the base case (`left >= right`) and the recursive step that shrinks the problem by 2 elements
- LeetCode 231 | Power of Two | Easy | https://leetcode.com/problems/power-of-two/ | Recursively divide n by 2 until reaching base case `n == 1` or an odd number
- LeetCode 21 | Merge Two Sorted Lists | Easy | https://leetcode.com/problems/merge-two-sorted-lists/ | Define list merging recursively: pick the smaller head and set its `next` to the recursive merge
- LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Contrast iterative pointer reversal with recursive reversal on the return path
- LeetCode 50 | Pow(x, n) | Medium | https://leetcode.com/problems/powx-n/ | Shrink problem size by half on every recursive call (`n / 2`)
- LeetCode 779 | K-th Symbol in Grammar | Medium | https://leetcode.com/problems/k-th-symbol-in-grammar/ | Trace a value in row n back to its parent in row n - 1 without building the rows
interview-questions:
- What two parts must every recursive function have so it does not run forever?
- How do you prove that a recursive function terminates for all valid inputs?
- What is tail recursion, and what does the `tailrec` modifier in Kotlin actually compile down to in bytecode?
- When is a recursive solution cleaner to read than an iterative loop, and when is it dangerous in production?

## dsa/recursion/the-call-stack
problems:
- LeetCode 104 | Maximum Depth of Binary Tree | Easy | https://leetcode.com/problems/maximum-depth-of-binary-tree/ | See how maximum recursion call-stack depth directly matches the height of the tree
- LeetCode 94 | Binary Tree Inorder Traversal | Easy | https://leetcode.com/problems/binary-tree-inorder-traversal/ | Replace the JVM call stack with an explicit `ArrayDeque` stack to see what frames store
- LeetCode 394 | Decode String | Medium | https://leetcode.com/problems/decode-string/ | Compare saving local variables on the recursive call stack vs pushing state onto an explicit stack
- LeetCode 341 | Flatten Nested List Iterator | Medium | https://leetcode.com/problems/flatten-nested-list-iterator/ | Unroll recursive tree traversal into a lazy on-demand iterator using an explicit stack of frames
- LeetCode 114 | Flatten Binary Tree to Linked List | Medium | https://leetcode.com/problems/flatten-binary-tree-to-linked-list/ | Compare O(h) call-stack recursion against O(1)-space Morris-style pointer threading
interview-questions:
- What exact data is stored inside a single stack frame when a function calls another function?
- What causes a `StackOverflowError` on the JVM, and roughly how deep can standard recursion go before crashing?
- Why is the space complexity of recursive DFS on a binary tree O(h) rather than O(n), and when does O(h) become O(n)?
- How can you convert any recursive function that risks `StackOverflowError` into an iterative function?

## dsa/recursion/repeated-work-in-recursion
problems:
- LeetCode 509 | Fibonacci Number | Easy | https://leetcode.com/problems/fibonacci-number/ | Observe how naive `fib(n-1) + fib(n-2)` explodes to O(2^n) calls and collapses to O(n) with a cache
- LeetCode 70 | Climbing Stairs | Easy | https://leetcode.com/problems/climbing-stairs/ | Map a counting problem to the Fibonacci recurrence and eliminate duplicate subtrees
- LeetCode 1137 | N-th Tribonacci Number | Easy | https://leetcode.com/problems/n-th-tribonacci-number/ | See O(3^n) branching collapse to O(n) when repeated states are saved
- LeetCode 95 | Unique Binary Search Trees II | Medium | https://leetcode.com/problems/unique-binary-search-trees-ii/ | Reuse recursively generated left and right subtrees across combinations
- LeetCode 894 | All Possible Full Binary Trees | Medium | https://leetcode.com/problems/all-possible-full-binary-trees/ | Cache lists of trees for smaller `n` so recursive subproblems aren't rebuilt repeatedly
interview-questions:
- Why does naive recursive `fib(50)` make billions of function calls when there are only 51 distinct values of `fib(0)..fib(50)`?
- How do you draw a recursion tree to spot whether a recursive algorithm has overlapping subproblems?
- Why doesn't Merge Sort suffer from repeated work even though it makes two recursive calls just like Fibonacci?
- How much time and space does adding a `HashMap` or array cache to naive recursive Fibonacci take?

## dsa/sorting/what-sorting-means
problems:
- LeetCode 905 | Sort Array By Parity | Easy | https://leetcode.com/problems/sort-array-by-parity/ | Partition elements by a binary key (even vs odd) and observe whether relative order is preserved
- LeetCode 1122 | Relative Sort Array | Easy | https://leetcode.com/problems/relative-sort-array/ | Sort items according to a custom rank order with ties broken by natural value
- LeetCode 937 | Reorder Data in Log Files | Medium | https://leetcode.com/problems/reorder-data-in-log-files/ | Apply stability so digit-logs stay in their original relative order while letter-logs sort by content then identifier
- LeetCode 791 | Custom Sort String | Medium | https://leetcode.com/problems/custom-sort-string/ | Define a custom total ordering over characters and sort in O(n)
- LeetCode 179 | Largest Number | Medium | https://leetcode.com/problems/largest-number/ | Define a transitive comparator `(b + a).compareTo(a + b)` to order strings for maximum concatenation
interview-questions:
- What does it mean for a sorting algorithm to be *stable*, and give a UI example where an unstable sort breaks user expectations?
- Which classic sorting algorithms are stable (Merge Sort, Insertion Sort) and which are unstable (Quick Sort, Heap Sort)?
- What are the three mathematical rules (reflexivity, antisymmetry, transitivity) that a custom `Comparator` must obey so `sortWith` doesn't throw `IllegalArgumentException`?
- How do you sort a list of employees by department ascending, and for ties, by salary descending in Kotlin?

## dsa/sorting/simple-sorts
problems:
- HackerRank (Algorithms > Sorting) | Insertion Sort - Part 1 | Easy | https://www.hackerrank.com/challenges/insertionsort1/problem | Shift larger elements right to insert a single out-of-order value into its sorted slot
- HackerRank (Algorithms > Sorting) | Insertion Sort - Part 2 | Easy | https://www.hackerrank.com/challenges/insertionsort2/problem | Run full Insertion Sort step-by-step and watch the sorted prefix grow
- HackerRank (Algorithms > Sorting) | Running Time of Algorithms | Easy | https://www.hackerrank.com/challenges/runningtime/problem | Count the exact number of shifts Insertion Sort makes and relate it to inversions
- LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Improve on simple swapping with Dutch National Flag 3-way partitioning in one pass
- LeetCode 147 | Insertion Sort List | Medium | https://leetcode.com/problems/insertion-sort-list/ | Implement Insertion Sort on a singly linked list without array indexing
interview-questions:
- Why does Insertion Sort run in O(n) time on an already-sorted or nearly-sorted array while Selection Sort still takes O(n^2)?
- Why do industrial sorting libraries like TimSort switch to Insertion Sort for small subarrays (typically n <= 32)?
- How many swaps does Selection Sort perform in the worst case compared to Bubble Sort?
- If an array has only `k` inversions (pairs out of order), what is the exact runtime of Insertion Sort?

## dsa/sorting/merge-sort
problems:
- LeetCode 88 | Merge Sorted Array | Easy | https://leetcode.com/problems/merge-sorted-array/ | Master the two-pointer `merge` step that combines two sorted halves in O(n)
- LeetCode 21 | Merge Two Sorted Lists | Easy | https://leetcode.com/problems/merge-two-sorted-lists/ | Merge two sorted linked lists in O(1) auxiliary space
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implement full top-down or bottom-up Merge Sort to guarantee O(n log n) worst-case time
- LeetCode 148 | Sort List | Medium | https://leetcode.com/problems/sort-list/ | Sort a linked list in O(n log n) time using slow/fast midpoint splitting and merging
- LeetCode 315 | Count of Smaller Numbers After Self | Hard | https://leetcode.com/problems/count-of-smaller-numbers-after-self/ | Piggyback on Merge Sort's merge step to count right-side smaller elements in O(n log n)
- LeetCode 23 | Merge k Sorted Lists | Hard | https://leetcode.com/problems/merge-k-sorted-lists/ | Apply divide-and-conquer pairwise merging across k lists in O(N log k) time
interview-questions:
- Why does Merge Sort guarantee O(n log n) time even in the worst case, and what is its space trade-off on arrays?
- Why is Merge Sort the preferred algorithm for sorting linked lists over Quick Sort?
- How does bottom-up iterative Merge Sort avoid recursion stack frames altogether?
- How can you modify the `merge` step of Merge Sort to count the total number of inversions in an array in O(n log n)?

## dsa/sorting/quick-sort
problems:
- HackerRank (Algorithms > Sorting) | Quicksort 1 - Partition | Easy | https://www.hackerrank.com/challenges/quicksort1/problem | Partition an array around a pivot into `less`, `equal`, and `greater` groups
- LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Master 3-way Dutch National Flag partitioning to handle duplicate keys without O(n^2) degradation
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implement Quick Sort with a randomized pivot and 3-way partition to pass all-duplicate test cases
- LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Use Quickselect partitioning to recurse on only one side in O(n) average time
- LeetCode 973 | K Closest Points to Origin | Medium | https://leetcode.com/problems/k-closest-points-to-origin/ | Partition points by squared Euclidean distance in O(n) average time without full sorting
interview-questions:
- What input causes naive Quick Sort (picking the first or last element as pivot) to degrade to O(n^2), and how do randomized pivot or median-of-three fix it?
- Why does standard 2-way Quick Sort degrade to O(n^2) when all elements in the array are identical, and how does 3-way partitioning solve this?
- Even though Quick Sort and Merge Sort are both O(n log n) on average, why is Quick Sort usually faster in practice for primitive arrays?
- What is the worst-case and best-case recursion stack space of Quick Sort, and how does tail-call recursion on the larger partition bound space to O(log n)?

## dsa/sorting/sorting-in-the-standard-library
problems:
- LeetCode 2418 | Sort the People | Easy | https://leetcode.com/problems/sort-the-people/ | Pair indices or objects and sort using a descending key selector
- LeetCode 1636 | Sort Array by Increasing Frequency | Easy | https://leetcode.com/problems/sort-array-by-increasing-frequency/ | Chain primary and secondary comparators (`compareBy` then `thenByDescending`)
- LeetCode 56 | Merge Intervals | Medium | https://leetcode.com/problems/merge-intervals/ | Sort intervals by start time `sortedBy { it[0] }` as the O(n log n) preprocessing step
- LeetCode 406 | Queue Reconstruction by Height | Medium | https://leetcode.com/problems/queue-reconstruction-by-height/ | Sort by height descending and k ascending using a composite comparator
- LeetCode 354 | Russian Doll Envelopes | Hard | https://leetcode.com/problems/russian-doll-envelopes/ | Sort width ascending and height descending on ties so 2D nesting reduces to 1D LIS
interview-questions:
- In Kotlin, what is the memory and performance difference between `list.sortedBy { it.age }` and `mutableList.sortBy { it.age }`?
- Which sorting algorithms do `Arrays.sort(IntArray)` (Dual-Pivot Quicksort) and `Arrays.sort(Array<T>)` (TimSort) use on the JVM, and why do they differ?
- Why is TimSort able to sort real-world partially ordered data in as little as O(n) time?
- If your key function is expensive to compute, why should you avoid calling it repeatedly inside a `Comparator` without caching?

## dsa/searching/binary-search
problems:
- LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Implement classic exact-match binary search and use `low + (high - low) / 2`
- LeetCode 374 | Guess Number Higher or Lower | Easy | https://leetcode.com/problems/guess-number-higher-or-lower/ | Binary search an implicit range `1..n` using an external comparison API
- LeetCode 69 | Sqrt(x) | Easy | https://leetcode.com/problems/sqrtx/ | Binary search for the largest integer whose square does not exceed x
- LeetCode 74 | Search a 2D Matrix | Medium | https://leetcode.com/problems/search-a-2d-matrix/ | Treat an m x n sorted matrix as a virtual 1D array of length `m * n` using `mid / cols` and `mid % cols`
- LeetCode 33 | Search in Rotated Sorted Array | Medium | https://leetcode.com/problems/search-in-rotated-sorted-array/ | Identify which half of a rotated array is sorted on each step to maintain O(log n) search
interview-questions:
- Why does `val mid = (low + high) / 2` risk integer overflow for large arrays, and how does `low + (high - low) / 2` or `(low + high) ushr 1` prevent it?
- How many comparisons at most does binary search need to search a sorted list of 1,000,000 items?
- Can you use binary search on a sorted `LinkedList` to get O(log n) time? Why or why not?
- What does Kotlin's `list.binarySearch(target)` return when the element is *not* found, and how do you recover the insertion index from that negative number?

## dsa/searching/binary-search-boundaries
problems:
- LeetCode 35 | Search Insert Position | Easy | https://leetcode.com/problems/search-insert-position/ | Find the lower bound (first index `>= target`) cleanly without off-by-one errors
- LeetCode 278 | First Bad Version | Easy | https://leetcode.com/problems/first-bad-version/ | Binary search a boolean predicate `[false, false, true, true]` to find the exact boundary
- LeetCode 34 | Find First and Last Position of Element in Sorted Array | Medium | https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/ | Run both lower-bound and upper-bound binary searches to handle duplicate targets
- LeetCode 153 | Find Minimum in Rotated Sorted Array | Medium | https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/ | Converge `low < high` with `high = mid` vs `low = mid + 1` on a boundary condition
- LeetCode 162 | Find Peak Element | Medium | https://leetcode.com/problems/find-peak-element/ | Use slope comparison `nums[mid] < nums[mid + 1]` to converge on a local peak in O(log n)
- LeetCode 875 | Koko Eating Bananas | Medium | https://leetcode.com/problems/koko-eating-bananas/ | Binary search the answer space `[1, maxPile]` to find the minimum feasible rate
- LeetCode 410 | Split Array Largest Sum | Hard | https://leetcode.com/problems/split-array-largest-sum/ | Binary search on the minimum possible maximum subarray sum using a greedy feasibility check
interview-questions:
- When does `low = mid` inside a `while (low < high)` loop cause an infinite loop when `high == low + 1`, and how does `mid = low + (high - low + 1) / 2` fix it?
- What is the difference between `lower_bound` (first element `>= target`) and `upper_bound` (first element `> target`)?
- How do you recognize a "Binary Search on Answer" problem where the input array itself is not sorted?
- What loop invariant holds true at the start and end of every iteration in a `while (low < high)` boundary binary search?

## dsa/trees/what-is-a-tree
problems:
- LeetCode 2236 | Root Equals Sum of Children | Easy | https://leetcode.com/problems/root-equals-sum-of-children/ | Inspect a parent node and its immediate `left` and `right` child references
- LeetCode 104 | Maximum Depth of Binary Tree | Easy | https://leetcode.com/problems/maximum-depth-of-binary-tree/ | Compute tree height recursively from leaf nodes (`null` = 0) up to the root
- LeetCode 100 | Same Tree | Easy | https://leetcode.com/problems/same-tree/ | Verify structural and value equality of two hierarchical trees simultaneously
- LeetCode 226 | Invert Binary Tree | Easy | https://leetcode.com/problems/invert-binary-tree/ | Recursively swap child subtrees at every internal node
- LeetCode 101 | Symmetric Tree | Easy | https://leetcode.com/problems/symmetric-tree/ | Check mirror symmetry across left and right subtrees
- LeetCode 543 | Diameter of Binary Tree | Easy | https://leetcode.com/problems/diameter-of-binary-tree/ | Distinguish the longest path *through* a node (`leftHeight + rightHeight`) from the height *returned* to its parent
interview-questions:
- Why does an Android View hierarchy or Compose UI tree form a tree rather than a general graph with cycles?
- What is the difference between the *depth* of a node and the *height* of a node in a tree?
- If a tree has `n` nodes, how many edges does it have, and how many paths exist from the root to any given node?
- What is the difference between a full binary tree, a complete binary tree, and a perfect binary tree?

## dsa/trees/walking-a-tree
problems:
- LeetCode 144 | Binary Tree Preorder Traversal | Easy | https://leetcode.com/problems/binary-tree-preorder-traversal/ | Visit `root -> left -> right` both recursively and iteratively
- LeetCode 94 | Binary Tree Inorder Traversal | Easy | https://leetcode.com/problems/binary-tree-inorder-traversal/ | Visit `left -> root -> right` to process left subtrees before parent nodes
- LeetCode 145 | Binary Tree Postorder Traversal | Easy | https://leetcode.com/problems/binary-tree-postorder-traversal/ | Visit `left -> right -> root` when children must be evaluated before their parent
- LeetCode 112 | Path Sum | Easy | https://leetcode.com/problems/path-sum/ | Pass top-down state from root to leaf during a depth-first walk
- LeetCode 105 | Construct Binary Tree from Preorder and Inorder Traversal | Medium | https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/ | Exploit how Preorder identifies the root while Inorder splits left and right subtrees
- LeetCode 236 | Lowest Common Ancestor of a Binary Tree | Medium | https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/ | Propagate found targets bottom-up in postorder to identify where two paths meet
- LeetCode 124 | Binary Tree Maximum Path Sum | Hard | https://leetcode.com/problems/binary-tree-maximum-path-sum/ | Combine postorder subtree gains with a global maximum tracker
interview-questions:
- When measuring and laying out an Android UI tree, which steps naturally act like a top-down Preorder walk and which act like a bottom-up Postorder walk?
- Why can't you uniquely reconstruct a general binary tree from Preorder and Postorder traversals alone without Inorder?
- When deleting or freeing every node in a tree, why does Postorder traversal make the most sense?
- How do you write an iterative Inorder traversal using an explicit stack?

## dsa/trees/level-order-walk
problems:
- LeetCode 102 | Binary Tree Level Order Traversal | Medium | https://leetcode.com/problems/binary-tree-level-order-traversal/ | Use a FIFO queue and snapshot `queue.size` at the start of each loop to group nodes by level
- LeetCode 111 | Minimum Depth of Binary Tree | Easy | https://leetcode.com/problems/minimum-depth-of-binary-tree/ | See why level-order BFS finds the shallowest leaf faster than DFS on a lopsided tree
- LeetCode 637 | Average of Levels in Binary Tree | Easy | https://leetcode.com/problems/average-of-levels-in-binary-tree/ | Aggregate per-level statistics during a breadth-first walk
- LeetCode 199 | Binary Tree Right Side View | Medium | https://leetcode.com/problems/binary-tree-right-side-view/ | Capture the last node processed in each level of a queue-driven walk
- LeetCode 103 | Binary Tree Zigzag Level Order Traversal | Medium | https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/ | Alternate left-to-right and right-to-left collection on each level using a deque
- LeetCode 297 | Serialize and Deserialize Binary Tree | Hard | https://leetcode.com/problems/serialize-and-deserialize-binary-tree/ | Encode and reconstruct a binary tree using level-order BFS with `null` markers
interview-questions:
- In a single-queue level-order traversal, how do you know where level `k` ends and level `k + 1` begins without storing the depth on every node?
- When finding the minimum depth of a binary tree with 100,000 nodes where the left branch has depth 1 and the right branch has depth 99,999, why is BFS vastly faster than DFS?
- What is the worst-case space complexity of a level-order BFS queue on a balanced binary tree of `n` nodes vs a DFS recursion stack?
- Why is the widest level of a complete binary tree roughly `n / 2` nodes?

## dsa/bst/bst-search-and-insert
problems:
- LeetCode 700 | Search in a Binary Search Tree | Easy | https://leetcode.com/problems/search-in-a-binary-search-tree/ | Prune half the tree at every step using the BST ordering property
- LeetCode 938 | Range Sum of BST | Easy | https://leetcode.com/problems/range-sum-of-bst/ | Skip entire left or right subtrees when `node.val < low` or `node.val > high`
- LeetCode 701 | Insert into a Binary Search Tree | Medium | https://leetcode.com/problems/insert-into-a-binary-search-tree/ | Walk down to the unique `null` leaf slot where a new key belongs and attach it
- LeetCode 98 | Validate Binary Search Tree | Medium | https://leetcode.com/problems/validate-binary-search-tree/ | Pass `(min, max)` bounds down so every descendant—not just the immediate child—obeys the BST invariant
- LeetCode 230 | Kth Smallest Element in a BST | Medium | https://leetcode.com/problems/kth-smallest-element-in-a-bst/ | Use the fact that an Inorder traversal of a BST visits keys in strictly sorted order
- LeetCode 235 | Lowest Common Ancestor of a Binary Search Tree | Medium | https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/ | Find the split point where `p` and `q` diverge in O(h) time and O(1) space
- LeetCode 450 | Delete Node in a BST | Medium | https://leetcode.com/problems/delete-node-in-a-bst/ | Handle the 3 deletion cases: leaf, one child, and two children (replacing with the inorder successor)
interview-questions:
- Why isn't checking `node.left.val < node.val < node.right.val` at every node enough to prove a binary tree is a valid BST?
- Which traversal order of a Binary Search Tree produces its elements in sorted ascending order, and why?
- How do you delete a BST node that has *two* non-null children while preserving the BST ordering property?
- When would you choose `TreeMap` / `TreeSet` (Red-Black BST) over `HashMap` / `HashSet` in Kotlin?

## dsa/bst/bst-shape-and-balance
problems:
- LeetCode 108 | Convert Sorted Array to Binary Search Tree | Easy | https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/ | Repeatedly pick the middle element as root so the resulting BST has minimum height O(log n)
- LeetCode 110 | Balanced Binary Tree | Easy | https://leetcode.com/problems/balanced-binary-tree/ | Check in O(n) postorder time whether every node's left and right subtrees differ in height by at most 1
- LeetCode 897 | Increasing Order Search Tree | Easy | https://leetcode.com/problems/increasing-order-search-tree/ | Deliberately flatten a BST into a right-skewed chain to see what a degenerate O(n) BST looks like
- LeetCode 109 | Convert Sorted List to Binary Search Tree | Medium | https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/ | Build a height-balanced BST from a sorted linked list in O(n) or O(n log n)
- LeetCode 1382 | Balance a Binary Search Tree | Medium | https://leetcode.com/problems/balance-a-binary-search-tree/ | Extract sorted keys via Inorder traversal and rebuild a balanced BST of height O(log n)
interview-questions:
- What shape does a plain Binary Search Tree take if you insert `[1, 2, 3, 4, 5, 6, 7]` in sorted order, and what happens to search complexity?
- Why must we repeatedly pick the median when building a balanced BST from a sorted array?
- How do self-balancing trees like AVL trees and Red-Black trees maintain O(log n) height during arbitrary insertions and deletions?
- Why did Java 8 choose Red-Black trees as the fallback for overloaded `HashMap` buckets instead of plain BSTs?

## dsa/heaps/priority-queue-idea
problems:
- LeetCode 1046 | Last Stone Weight | Easy | https://leetcode.com/problems/last-stone-weight/ | Repeatedly extract the two heaviest elements and push their difference back into a max-heap
- LeetCode 703 | Kth Largest Element in a Stream | Easy | https://leetcode.com/problems/kth-largest-element-in-a-stream/ | Maintain a min-heap of size k so the kth largest element is always at `peek()` in O(1)
- LeetCode 347 | Top K Frequent Elements | Medium | https://leetcode.com/problems/top-k-frequent-elements/ | Use a size-k min-heap ordered by frequency to avoid sorting all n distinct keys
- LeetCode 621 | Task Scheduler | Medium | https://leetcode.com/problems/task-scheduler/ | Always schedule the most frequent remaining task first using a max-heap and cooldown queue
- LeetCode 23 | Merge k Sorted Lists | Hard | https://leetcode.com/problems/merge-k-sorted-lists/ | Keep the current head of each of the k lists in a min-heap to always pull the global minimum in O(log k)
- LeetCode 295 | Find Median from Data Stream | Hard | https://leetcode.com/problems/find-median-from-data-stream/ | Balance a max-heap (lower half) and a min-heap (upper half) for O(log n) insert and O(1) median
interview-questions:
- Counterintuitively, why do you use a *min-heap* of size `k` instead of a max-heap to find the *kth largest* element in a stream?
- In Kotlin/Java, does `PriorityQueue<Int>()` default to a min-heap or a max-heap, and how do you invert it?
- Why is finding the Top K items with a size-k heap O(n log k) better than sorting the entire array in O(n log n) when k is small?
- How does the two-heap pattern (`maxHeap` for the smaller half, `minHeap` for the larger half) maintain the median of a live data stream?

## dsa/heaps/how-a-heap-works
problems:
- HackerRank (Data Structures > Heap) | QHEAP1 | Easy | https://www.hackerrank.com/challenges/qheap1/problem | Practice core heap operations: insert, delete arbitrary element, and print minimum
- LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implement in-place Heap Sort using `siftDown` (`heapify`) in O(n log n) time and O(1) auxiliary space
- LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Build a heap in O(n) using bottom-up `heapify` and extract k times
- HackerRank (Data Structures > Heap) | Jesse and Cookies | Easy | https://www.hackerrank.com/challenges/jesse-and-cookies/problem | Repeatedly pop the two smallest elements and push their combined value until threshold is met
- LeetCode 373 | Find K Pairs with Smallest Sums | Medium | https://leetcode.com/problems/find-k-pairs-with-smallest-sums/ | Frontier-expand candidate index pairs in a min-heap without generating all n * m pairs
interview-questions:
- Why can a binary heap be stored in a plain flat array without any `left` or `right` node pointers?
- For a 0-indexed array heap, what are the exact index formulas for `parent(i)`, `leftChild(i)`, and `rightChild(i)`?
- Why does building a heap from an existing array of `n` elements bottom-up (`siftDown` from `n/2 - 1` down to `0`) take O(n) time instead of O(n log n)?
- Why doesn't a binary heap support O(log n) search for an arbitrary value the way a Binary Search Tree does?

## dsa/graphs/what-is-a-graph
problems:
- LeetCode 1791 | Find Center of Star Graph | Easy | https://leetcode.com/problems/find-center-of-star-graph/ | Reason about node degrees where the center vertex has degree `n - 1`
- LeetCode 997 | Find the Town Judge | Easy | https://leetcode.com/problems/find-the-town-judge/ | Distinguish `in-degree` (`n - 1`) from `out-degree` (`0`) in a directed graph
- LeetCode 1971 | Find if Path Exists in Graph | Easy | https://leetcode.com/problems/find-if-path-exists-in-graph/ | Check connectivity between two vertices in an undirected graph
- LeetCode 841 | Keys and Rooms | Medium | https://leetcode.com/problems/keys-and-rooms/ | Model rooms and keys as a directed graph and test whether all vertices are reachable from vertex 0
- LeetCode 785 | Is Graph Bipartite? | Medium | https://leetcode.com/problems/is-graph-bipartite/ | 2-color a graph to check whether it contains any odd-length cycles
interview-questions:
- What is the difference between a directed graph and an undirected graph, and how is a tree just a special case of a graph?
- In a social network like Instagram (following) vs Facebook (friends) vs Google Maps (road travel times), which are directed, undirected, and weighted?
- What do `in-degree` and `out-degree` mean in a directed graph?
- What is a Connected Component in an undirected graph, and what is a DAG (Directed Acyclic Graph)?

## dsa/graphs/storing-a-graph
problems:
- LeetCode 997 | Find the Town Judge | Easy | https://leetcode.com/problems/find-the-town-judge/ | Convert an edge list `Array<IntArray>` into degree arrays
- LeetCode 1971 | Find if Path Exists in Graph | Easy | https://leetcode.com/problems/find-if-path-exists-in-graph/ | Build an adjacency list `Array(n) { mutableListOf<Int>() }` from an undirected edge list
- LeetCode 133 | Clone Graph | Medium | https://leetcode.com/problems/clone-graph/ | Traverse and deep-copy an object-pointer adjacency representation using a visited map
- LeetCode 547 | Number of Provinces | Medium | https://leetcode.com/problems/number-of-provinces/ | Traverse a graph given directly as an `n x n` adjacency matrix `isConnected[i][j]`
- LeetCode 1557 | Minimum Number of Vertices to Reach All Nodes | Medium | https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/ | Represent a DAG compactly to find all vertices with in-degree 0
interview-questions:
- What are the space and time trade-offs between an Adjacency Matrix (`O(V^2)`) and an Adjacency List (`O(V + E)`)?
- Why is an Adjacency List almost always preferred over an Adjacency Matrix for sparse real-world graphs like road networks?
- When given an undirected edge `[u, v]`, why must you add `v` to `adj[u]` *and* `u` to `adj[v]` when building an adjacency list?
- In grid problems like `Number of Islands`, why don't you need to build an explicit adjacency list when the 2D array already acts as an implicit graph?

## dsa/graphs/breadth-first-search
problems:
- LeetCode 733 | Flood Fill | Easy | https://leetcode.com/problems/flood-fill/ | Run a basic queue-based BFS over 4-directional grid neighbors
- LeetCode 994 | Rotting Oranges | Medium | https://leetcode.com/problems/rotting-oranges/ | Run multi-source BFS starting with all initial rotten oranges in the queue at level 0
- LeetCode 1091 | Shortest Path in Binary Matrix | Medium | https://leetcode.com/problems/shortest-path-in-binary-matrix/ | Find the fewest 8-directional steps from top-left to bottom-right in an unweighted grid
- LeetCode 752 | Open the Lock | Medium | https://leetcode.com/problems/open-the-lock/ | Model 4-digit lock states as an implicit graph and use BFS to find minimum wheel turns
- LeetCode 127 | Word Ladder | Hard | https://leetcode.com/problems/word-ladder/ | Find the shortest word-transformation sequence in an unweighted word graph using BFS
interview-questions:
- Why does Breadth-First Search guarantee the shortest path (fewest edges) in an unweighted graph while Depth-First Search does not?
- In BFS, why must you mark a node as `visited` at the exact moment you *enqueue* it rather than waiting until you *dequeue* it?
- How does multi-source BFS (like `Rotting Oranges`) find the minimum distance from *any* starting node in a single O(V + E) pass?
- When does BFS fail to find the shortest path, requiring Dijkstra's algorithm instead?

## dsa/graphs/depth-first-search
problems:
- LeetCode 733 | Flood Fill | Easy | https://leetcode.com/problems/flood-fill/ | Explore connected regions recursively until hitting boundaries or different colors
- LeetCode 200 | Number of Islands | Medium | https://leetcode.com/problems/number-of-islands/ | Count connected components in a 2D grid by sinking each island with DFS
- LeetCode 695 | Max Area of Island | Medium | https://leetcode.com/problems/max-area-of-island/ | Accumulate and return the size of each connected component via recursive DFS
- LeetCode 797 | All Paths From Source to Target | Medium | https://leetcode.com/problems/all-paths-from-source-to-target/ | Explore every path from node `0` to `n - 1` in a DAG using DFS with path backtracking
- LeetCode 130 | Surrounded Regions | Medium | https://leetcode.com/problems/surrounded-regions/ | Run boundary-seeded DFS from the edges inward to mark regions that cannot be captured
- LeetCode 329 | Longest Increasing Path in a Matrix | Hard | https://leetcode.com/problems/longest-increasing-path-in-a-matrix/ | Combine DFS with memoization on a directed acyclic grid graph
interview-questions:
- Why must graph DFS maintain a `visited` set whereas tree DFS does not?
- How do you detect a cycle in an *undirected* graph vs a *directed* graph using DFS?
- In directed graph DFS, why do you need three states (`UNVISITED`, `VISITING` on current path, `VISITED` completely done) to detect a cycle?
- What is the time and space complexity of DFS on a graph with `V` vertices and `E` edges stored as an adjacency list?

## dsa/graphs/dependency-order
problems:
- LeetCode 207 | Course Schedule | Medium | https://leetcode.com/problems/course-schedule/ | Detect whether prerequisite dependencies contain a circular deadlock using Kahn's BFS or 3-color DFS
- LeetCode 210 | Course Schedule II | Medium | https://leetcode.com/problems/course-schedule-ii/ | Output a valid topological execution order for tasks with prerequisites
- LeetCode 802 | Find Eventual Safe States | Medium | https://leetcode.com/problems/find-eventual-safe-states/ | Use reverse-edge topological sorting (Kahn's algorithm) or cycle detection to find safe nodes
- LeetCode 2115 | Find All Possible Recipes from Given Supplies | Medium | https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/ | Model recipes and ingredients as a dependency DAG and resolve buildable items via in-degrees
- LeetCode 329 | Longest Increasing Path in a Matrix | Hard | https://leetcode.com/problems/longest-increasing-path-in-a-matrix/ | Find the longest dependency chain in an implicit DAG
interview-questions:
- How does Gradle determine which Android modules can compile first and which modules must wait, and what error occurs if Module A depends on Module B and B depends on A?
- Walk through Kahn's algorithm for topological sorting using `in-degree` counts and a queue.
- Why does topological sort only exist for Directed Acyclic Graphs (DAGs)?
- How can you tell at the end of Kahn's algorithm whether the dependency graph contained a cycle?

## dsa/tries/what-is-a-trie
problems:
- LeetCode 14 | Longest Common Prefix | Easy | https://leetcode.com/problems/longest-common-prefix/ | Understand shared string prefixes before generalizing to a branching prefix tree
- LeetCode 208 | Implement Trie (Prefix Tree) | Medium | https://leetcode.com/problems/implement-trie-prefix-tree/ | Build `TrieNode` with `children` array and `isEndOfWord` flag to support `insert`, `search`, and `startsWith`
- LeetCode 211 | Design Add and Search Words Data Structure | Medium | https://leetcode.com/problems/design-add-and-search-words-data-structure/ | Combine Trie lookup with DFS branching when encountering a `.` wildcard character
- LeetCode 648 | Replace Words | Medium | https://leetcode.com/problems/replace-words/ | Store dictionary roots in a Trie to find the shortest matching prefix of each word in O(L) time
- LeetCode 1268 | Search Suggestions System | Medium | https://leetcode.com/problems/search-suggestions-system/ | Return the top 3 lexicographically smallest autocomplete matches after each typed character
- LeetCode 212 | Word Search II | Hard | https://leetcode.com/problems/word-search-ii/ | Prune grid backtracking simultaneously across thousands of dictionary words using a Trie
interview-questions:
- Why is a Trie better than a `HashSet<String>` for implementing an autocomplete search bar?
- What is the time complexity of `startsWith(prefix)` in a Trie of 1,000,000 words when the prefix has length `L = 5`?
- Why is the `isEndOfWord` boolean needed on a `TrieNode` if `"app"` and `"apple"` are both inserted?
- When should a `TrieNode` store children in `Array<TrieNode?>(26)` vs `HashMap<Char, TrieNode>`?

## dsa/patterns/two-pointers
problems:
- LeetCode 125 | Valid Palindrome | Easy | https://leetcode.com/problems/valid-palindrome/ | Walk `left` and `right` pointers inward while skipping non-alphanumeric characters in O(n) time and O(1) space
- LeetCode 283 | Move Zeroes | Easy | https://leetcode.com/problems/move-zeroes/ | Use same-direction slow (`write`) and fast (`read`) pointers to partition in-place
- LeetCode 167 | Two Sum II - Input Array Is Sorted | Medium | https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/ | Exploit sorted order to move `left++` when sum is too small and `right--` when sum is too large
- LeetCode 11 | Container With Most Water | Medium | https://leetcode.com/problems/container-with-most-water/ | Greedily advance the shorter boundary pointer inward to maximize area in O(n)
- LeetCode 15 | 3Sum | Medium | https://leetcode.com/problems/3sum/ | Fix one element and run a converging two-pointer sweep with duplicate skipping in O(n^2)
- LeetCode 42 | Trapping Rain Water | Hard | https://leetcode.com/problems/trapping-rain-water/ | Maintain `leftMax` and `rightMax` across two converging pointers to compute trapped water in O(1) space
interview-questions:
- In `Two Sum II` on a sorted array, why is it safe to discard the entire rest of the row/column when moving `left++` or `right--`?
- What are the two main flavors of the Two Pointers pattern (opposite-end converging pointers vs same-direction slow/fast pointers)?
- In `Container With Most Water`, why must you always move the pointer pointing to the *shorter* line rather than the taller line?
- Why does the two-pointer approach for `Two Sum` require the array to be sorted first, whereas the `HashMap` approach works on unsorted arrays?

## dsa/patterns/sliding-window
problems:
- LeetCode 643 | Maximum Average Subarray I | Easy | https://leetcode.com/problems/maximum-average-subarray-i/ | Slide a fixed-size window of length k by adding `nums[i]` and subtracting `nums[i - k]`
- LeetCode 121 | Best Time to Buy and Sell Stock | Easy | https://leetcode.com/problems/best-time-to-buy-and-sell-stock/ | Track the lowest buy price as the left anchor of an expanding window
- LeetCode 3 | Longest Substring Without Repeating Characters | Medium | https://leetcode.com/problems/longest-substring-without-repeating-characters/ | Expand `right` and advance `left` past duplicates to find the longest valid variable-size window
- LeetCode 209 | Minimum Size Subarray Sum | Medium | https://leetcode.com/problems/minimum-size-subarray-sum/ | Expand `right` until sum >= target, then shrink `left` as much as possible to minimize window length
- LeetCode 424 | Longest Repeating Character Replacement | Medium | https://leetcode.com/problems/longest-repeating-character-replacement/ | Maintain `windowLength - maxFreq <= k` over a sliding character frequency window
- LeetCode 76 | Minimum Window Substring | Hard | https://leetcode.com/problems/minimum-window-substring/ | Master the expand-until-valid / shrink-to-optimize template on character frequency maps
interview-questions:
- What is the difference between a fixed-size sliding window and a dynamic (variable-size) sliding window?
- Why does `Minimum Size Subarray Sum` sliding window work in O(n) only when all array numbers are positive, and what breaks if the array contains negative numbers?
- Why is the dynamic sliding window (`for right` with an inner `while` shrinking `left`) O(n) total time instead of O(n^2)?
- What state do you typically maintain inside a sliding window so you don't recompute the window's validity from scratch on every step?

## dsa/patterns/prefix-sums
problems:
- LeetCode 1480 | Running Sum of 1d Array | Easy | https://leetcode.com/problems/running-sum-of-1d-array/ | Build the foundational cumulative sum array `prefix[i] = prefix[i - 1] + nums[i]`
- LeetCode 303 | Range Sum Query - Immutable | Easy | https://leetcode.com/problems/range-sum-query-immutable/ | Answer arbitrary `sumRange(left, right)` queries in O(1) time using `prefix[right + 1] - prefix[left]`
- LeetCode 724 | Find Pivot Index | Easy | https://leetcode.com/problems/find-pivot-index/ | Compare left prefix sum against `totalSum - leftSum - nums[i]` in O(1) space
- LeetCode 560 | Subarray Sum Equals K | Medium | https://leetcode.com/problems/subarray-sum-equals-k/ | Combine running prefix sums with a `HashMap` to count subarrays summing to k (including negatives) in O(n)
- LeetCode 523 | Continuous Subarray Sum | Medium | https://leetcode.com/problems/continuous-subarray-sum/ | Store `prefixSum % k` remainders in a map to spot subarray multiples of k
- LeetCode 304 | Range Sum Query 2D - Immutable | Medium | https://leetcode.com/problems/range-sum-query-2d-immutable/ | Generalize prefix sums to 2D rectangles using inclusion-exclusion in O(1) per query
interview-questions:
- Why do we usually allocate a prefix sum array of size `n + 1` with `prefix[0] = 0` instead of size `n`?
- How does `Subarray Sum Equals K` combine prefix sums and a `HashMap` to handle arrays with negative numbers where a sliding window fails?
- In a 2D prefix sum grid, why does computing a sub-rectangle sum require adding back the top-left corner `prefix[r1][c1]` (inclusion-exclusion)?
- When the underlying array is updated frequently (`update(index, val)`), why do plain prefix sums become slow (O(n) per update) and what structure replaces them?

## dsa/patterns/choose-the-right-structure
problems:
- LeetCode 359 | Logger Rate Limiter | Easy | https://leetcode.com/problems/logger-rate-limiter/ | Choose a `HashMap<String, Int>` (or queue + set) to deduplicate messages within a 10-second window
- LeetCode 146 | LRU Cache | Medium | https://leetcode.com/problems/lru-cache/ | Combine a `HashMap` (for O(1) key lookup) with a doubly linked list (for O(1) recency reordering)
- LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Combine an `ArrayList` (for O(1) random index access) with a `HashMap` (for O(1) value-to-index lookup)
- LeetCode 981 | Time Based Key-Value Store | Medium | https://leetcode.com/problems/time-based-key-value-store/ | Combine a `HashMap<String, MutableList<Pair<Int, String>>>` with binary search on monotonic timestamps
- LeetCode 295 | Find Median from Data Stream | Hard | https://leetcode.com/problems/find-median-from-data-stream/ | Choose two opposite-ordered heaps over a sorted list to get O(log n) insertion and O(1) median
- LeetCode 460 | LFU Cache | Hard | https://leetcode.com/problems/lfu-cache/ | Combine a key map, a frequency-to-doubly-linked-list map, and `minFreq` tracking for all-O(1) operations
interview-questions:
- When a single data structure cannot satisfy all required O(1) or O(log n) operations, how do you combine two structures (like `HashMap` + `DoublyLinkedList` or `HashMap` + `ArrayList`)?
- Which data structure would you choose for: (a) undo/redo in a text editor, (b) autocomplete prefix matching, (c) finding the highest-priority background job, and (d) checking if a user ID is blocked?
- In `Time Based Key-Value Store`, why can you use a plain `ArrayList` + binary search instead of a `TreeMap` if timestamps are guaranteed to be strictly increasing?
- How do you translate problem constraints (`n <= 20`, `n <= 10^4`, `n <= 10^5`) into the target time complexity and candidate data structures before writing code?

## Self-check
The 10 problem numbers in `practice-dsa.md` to verify first during human review:
1. LeetCode 2418 — Sort the People (`dsa/sorting/sorting-in-the-standard-library`)
2. LeetCode 2115 — Find All Possible Recipes from Given Supplies (`dsa/graphs/dependency-order`)
3. LeetCode 2073 — Time Needed to Buy Tickets (`dsa/queues/what-is-a-queue`)
4. LeetCode 2236 — Root Equals Sum of Children (`dsa/trees/what-is-a-tree`)
5. LeetCode 1636 — Sort Array by Increasing Frequency (`dsa/sorting/sorting-in-the-standard-library`)
6. LeetCode 1614 — Maximum Nesting Depth of the Parentheses (`dsa/stacks/matching-brackets`)
7. LeetCode 1768 — Merge Strings Alternately (`dsa/strings/text-building-cost`)
8. LeetCode 1472 — Design Browser History (`dsa/linked-lists/linked-list-vs-arraylist`)
9. LeetCode 1268 — Search Suggestions System (`dsa/tries/what-is-a-trie`)
10. LeetCode 359 — Logger Rate Limiter (`dsa/patterns/choose-the-right-structure` — note: LeetCode Premium problem)