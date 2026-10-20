# practice: dsa (merged from two independent answers)

Tags: `[both]` = both sources gave the problem (high confidence); `[A]`/`[B]` = one source only (verify the number before relying on it).


## dsa/complexity/counting-steps
problems:
- [both] LeetCode 1480 | Running Sum of 1d Array | Easy | https://leetcode.com/problems/running-sum-of-1d-array/ | Count exact loop iterations and assignments for a single pass over n elements
- [both] LeetCode 1342 | Number of Steps to Reduce a Number to Zero | Easy | https://leetcode.com/problems/number-of-steps-to-reduce-a-number-to-zero/ | Directly simulate and count operations as input scales by halving and subtracting
- [both] LeetCode 204 | Count Primes | Medium | https://leetcode.com/problems/count-primes/ | Observe why trial division times out at large n while Sieve of Eratosthenes reduces total steps
- [A] LeetCode 1512 | Number of Good Pairs | Easy | https://leetcode.com/problems/number-of-good-pairs/ | Compare the exact n(n-1)/2 step count of nested loops against a single-pass frequency count
- [A] HackerRank (Algorithms > Implementation) | Find Digits | Easy | https://www.hackerrank.com/challenges/find-digits/problem | Count division steps proportional to the number of base-10 digits rather than value size
- [B] HackerRank | Running Time of Algorithms | Easy | https://www.hackerrank.com/challenges/runningtime/problem | [Algorithms > Sorting] Counts exact shifts performed by insertion sort on concrete inputs
- [B] LeetCode 268 | Missing Number | Easy | https://leetcode.com/problems/missing-number/ | Contrasts multi-pass counting with a single-pass O(n) arithmetic sum
interview-questions:
- If an algorithm takes 1,000 steps for n = 10 and 1,000,000 steps for n = 100, how many steps do you expect for n = 1,000?
- Why does counting inner-loop executions give a better runtime estimate than counting lines of source code?
- How many times does `i = i * 2` execute before exceeding n = 1,000,000?
- On Android's 16ms UI frame budget, roughly how many basic loop iterations can run on the main thread before dropping frames?
- Why do we count dominant operations instead of measuring wall-clock milliseconds when comparing two algorithms?
- If a loop runs n times and calls a helper that scans an array of size 100, how many total steps execute for n = 10, 100, and 1000?
- How does CPU caching make two O(n) loops with the same step count run at wildly different speeds on Android hardware?
- How many steps does a loop take if the counter doubles each iteration (i *= 2) instead of incrementing by 1?

## dsa/complexity/big-o-notation
problems:
- [both] LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Contrast O(log n) halving with O(n) linear scan as n grows
- [both] LeetCode 169 | Majority Element | Easy | https://leetcode.com/problems/majority-element/ | Compare O(n log n) sorting, O(n) hash map space, and O(n) time / O(1) space Boyer-Moore voting
- [both] LeetCode 50 | Pow(x, n) | Medium | https://leetcode.com/problems/powx-n/ | Turn O(n) multiplications into O(log n) via binary exponentiation
- [both] LeetCode 4 | Median of Two Sorted Arrays | Hard | https://leetcode.com/problems/median-of-two-sorted-arrays/ | Enforce a strict O(log(m + n)) time bound where an O(m + n) merge is too slow
- [A] LeetCode 53 | Maximum Subarray | Medium | https://leetcode.com/problems/maximum-subarray/ | Reduce O(n^2) subarray checks to O(n) using Kadane's algorithm
- [B] LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Demonstrates why a one-pass O(n) Dutch National Flag beats a two-pass O(n) count only in constant factors
interview-questions:
- Why does Big-O drop constants and lower-order terms like the 50 in O(2n + 50)?
- When can an O(n^2) algorithm outperform an O(n log n) algorithm in real production code?
- Is O(1) guaranteed to be instant, or does it only mean the cost does not grow with input size?
- What is the Big-O time and space complexity of copying an array of size n?
- What does O(n) guarantee about growth rate, and why does it ignore constants and lower-order terms?
- What is the time complexity of iterating over a Kotlin List of size n and calling contains() on a Set of size m inside the loop?
- Why is binary search O(log n) regardless of whether the logarithm base is 2 or 10?

## dsa/complexity/reading-complexity-in-code
problems:
- [both] LeetCode 283 | Move Zeroes | Easy | https://leetcode.com/problems/move-zeroes/ | Spot how repeatedly shifting elements inside a loop hides O(n^2) work and fix it in O(n)
- [both] LeetCode 209 | Minimum Size Subarray Sum | Medium | https://leetcode.com/problems/minimum-size-subarray-sum/ | Recognize why a `while` loop inside a `for` loop is still O(n) when both pointers only move forward
- [both] LeetCode 15 | 3Sum | Medium | https://leetcode.com/problems/3sum/ | Combine sorting O(n log n) with a nested two-pointer scan to achieve O(n^2) instead of O(n^3)
- [both] LeetCode 84 | Largest Rectangle in Histogram | Hard | https://leetcode.com/problems/largest-rectangle-in-histogram/ | Prove amortized O(n) complexity when each element is pushed and popped at most once
- [A] LeetCode 14 | Longest Common Prefix | Easy | https://leetcode.com/problems/longest-common-prefix/ | Analyze two nested loops where the bound depends on both string count and string length
- [B] HackerRank | Bubble Sort | Easy | https://www.hackerrank.com/challenges/ctci-bubble-sort/problem | [Interview Preparation Kit > Sorting] Classic nested loop exhibiting O(n^2) comparisons and swaps
interview-questions:
- Why isn't every `while` loop nested inside a `for` loop automatically O(n^2)?
- What hidden time complexity is triggered if you call `list.contains(x)` inside a `for` loop over `list` in Kotlin?
- What is the time complexity of an outer loop running n times and an inner loop running `n, n/2, n/4, ..., 1` times?
- How do you analyze the time complexity of a chain of Kotlin collection operators like `.filter { ... }.map { ... }.sortedBy { ... }`?
- Does every function with a while-loop nested inside a for-loop run in O(n^2) time? Give a counterexample.
- Which Kotlin standard library calls on Strings or Lists secretly hide an O(n) scan inside what looks like a single line?
- What is the time complexity of a loop where the outer index i runs from 1 to n and the inner index j increments by i each step (j += i)?
- How do you spot accidental O(n^2) complexity when binding RecyclerView items or recomposing LazyColumn rows in Android?

## dsa/complexity/time-vs-space
problems:
- [both] LeetCode 217 | Contains Duplicate | Easy | https://leetcode.com/problems/contains-duplicate/ | Trade O(n) extra memory in a HashSet to drop time from O(n log n) to O(n)
- [both] LeetCode 1 | Two Sum | Easy | https://leetcode.com/problems/two-sum/ | Spend O(n) space on a complement map to avoid an O(n^2) pair search
- [both] LeetCode 136 | Single Number | Easy | https://leetcode.com/problems/single-number/ | Reclaim O(1) space from an O(n) hash map solution by using XOR properties
- [both] LeetCode 238 | Product of Array Except Self | Medium | https://leetcode.com/problems/product-of-array-except-self/ | Eliminate O(n) prefix/suffix arrays by accumulating results directly in the output array
- [both] LeetCode 41 | First Missing Positive | Hard | https://leetcode.com/problems/first-missing-positive/ | Reuse the input array as an in-place hash table to keep auxiliary space at O(1)
- [A] LeetCode 287 | Find the Duplicate Number | Medium | https://leetcode.com/problems/find-the-duplicate-number/ | Solve in O(n) time and strict O(1) extra space using Floyd's cycle detection
interview-questions:
- On a memory-constrained Android device, when would you choose an O(n log n) in-place sort over an O(n) HashMap solution?
- Does modifying the input array in-place count as O(1) space in an interview, and what are the side effects for the caller?
- How does precomputing a lookup table trade startup memory for faster frame-by-frame queries?
- Why does recursion count toward space complexity even if you allocate no arrays or collections?
- On memory-constrained Android devices, when would you deliberately choose an O(n log n) in-place algorithm over an O(n) algorithm that allocates O(n) memory?
- What is the difference between auxiliary space complexity and total space complexity?
- How does Kotlin's IntArray vs Array<Int> (or List<Int>) affect memory overhead when trading space for speed?
- Does recursion count toward space complexity even if you allocate no collections inside the function?

## dsa/arrays/arrays-in-memory
problems:
- [both] LeetCode 189 | Rotate Array | Medium | https://leetcode.com/problems/rotate-array/ | Use three in-place array reversals to rotate contiguous memory in O(1) extra space
- [both] LeetCode 48 | Rotate Image | Medium | https://leetcode.com/problems/rotate-image/ | Map 2D row-and-column coordinates in-place by transposing and reversing rows
- [A] LeetCode 1920 | Build Array from Permutation | Easy | https://leetcode.com/problems/build-array-from-permutation/ | Practice direct O(1) random access via index arithmetic `nums[nums[i]]`
- [A] LeetCode 66 | Plus One | Easy | https://leetcode.com/problems/plus-one/ | Traverse contiguous memory backward and handle full-array reallocation on carry overflow
- [A] LeetCode 54 | Spiral Matrix | Medium | https://leetcode.com/problems/spiral-matrix/ | Navigate 2D array boundaries cleanly without out-of-bounds reads
- [B] HackerRank | Arrays - DS | Easy | https://www.hackerrank.com/challenges/arrays-ds/problem | [Data Structures > Arrays] Direct O(1) index addressing from both ends of a contiguous block
- [B] HackerRank | 2D Array - DS | Easy | https://www.hackerrank.com/challenges/2d-array/problem | [Data Structures > Arrays] Constant-offset 2D indexing across hourglass coordinates
- [B] LeetCode 121 | Best Time to Buy and Sell Stock | Easy | https://leetcode.com/problems/best-time-to-buy-and-sell-stock/ | Sequential contiguous memory scan maximizing cache locality
interview-questions:
- How does the CPU compute the memory address of `arr[500]` in a single step?
- In Kotlin, what is the memory layout difference between `IntArray` and `Array<Int>`?
- Why does iterating through a 2D array row-by-row run faster than column-by-column on modern CPU caches?
- Why must all elements in a primitive array have the exact same byte size for O(1) indexing to work?
- How does the CPU compute the exact memory address of array[500] in a single arithmetic instruction?
- Why is iterating row-by-row over a 2D array faster than iterating column-by-column?
- In Kotlin/JVM, what is the memory layout difference between IntArray and Array<Int>?
- Why must all elements in a primitive contiguous array have the exact same byte size?

## dsa/arrays/insert-and-delete-in-an-array
problems:
- [both] LeetCode 27 | Remove Element | Easy | https://leetcode.com/problems/remove-element/ | Avoid O(n^2) repeated shifting by overwriting deleted slots in a single O(n) pass
- [both] LeetCode 26 | Remove Duplicates from Sorted Array | Easy | https://leetcode.com/problems/remove-duplicates-from-sorted-array/ | Compact unique elements to the front of an array using a read/write pointer pair
- [both] LeetCode 88 | Merge Sorted Array | Easy | https://leetcode.com/problems/merge-sorted-array/ | Write from the back of the array to insert elements without overwriting unread values
- [both] HackerRank (Data Structures > Arrays) | Left Rotation | Easy | https://www.hackerrank.com/challenges/array-left-rotation/problem | See why shifting left one by one is O(n * d) while index-offsetting is O(n)
- [A] LeetCode 80 | Remove Duplicates from Sorted Array II | Medium | https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/ | Generalize in-place array compaction when up to two duplicates are allowed
- [B] LeetCode 1089 | Duplicate Zeros | Easy | https://leetcode.com/problems/duplicate-zeros/ | Simulates in-place middle insertions by counting shifts first and writing backward in O(n)
interview-questions:
- Why does `list.removeAt(0)` on an `ArrayList` of 100,000 items take O(n) time while `list.removeAt(list.lastIndex)` takes O(1)?
- How can you delete an element at index `i` from an unsorted `ArrayList` in O(1) time if order does not matter?
- If you need to remove half the items from a `MutableList` in Kotlin, why is `removeAll { ... }` much faster than calling `removeAt(i)` in a loop?
- How does `System.arraycopy` move a block of array elements faster than a manual `for` loop?
- Why does calling list.removeAt(0) inside a loop over an ArrayList turn an O(n) loop into O(n^2)?
- Why is merging or inserting into an array with spare capacity at the end best done from right to left?
- If order does not matter, how can you delete the element at index i of an ArrayList in O(1) time?
- How does System.arraycopy work under the hood when you insert an item in the middle of a Kotlin MutableList?

## dsa/arrays/how-arraylist-grows
problems:
- [both] LeetCode 1929 | Concatenation of Array | Easy | https://leetcode.com/problems/concatenation-of-array/ | Allocate a new array of size 2n and copy existing elements over
- [both] LeetCode 118 | Pascal's Triangle | Easy | https://leetcode.com/problems/pascals-triangle/ | Dynamically grow nested lists row by row with known capacities
- [both] HackerRank (Data Structures > Arrays) | Dynamic Array | Easy | https://www.hackerrank.com/challenges/dynamic-array/problem | Manage a collection of dynamically growing sequences under append and lookup queries
- [both] LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Combine a resizable `ArrayList` with a `HashMap` to get O(1) amortized append and swap-delete
- [both] LeetCode 443 | String Compression | Medium | https://leetcode.com/problems/string-compression/ | Compare appending to a resizable buffer against compacting an existing array in-place
interview-questions:
- If an `ArrayList` doubles its backing array whenever it fills up, why is the average cost of `add()` still O(1) amortized?
- Why would growing a backing array by a fixed +10 slots each time degrade `n` appends to O(n^2) total time?
- When should you pass `initialCapacity` to `ArrayList(capacity)` in Kotlin?
- Does removing elements from a Java/Kotlin `ArrayList` automatically shrink the backing array in memory?
- If resizing an ArrayList copies all n elements in O(n) time, why do we still say add() at the end is O(1) amortized?
- Why do dynamic arrays grow by a multiplicative factor (such as 1.5x or 2x) instead of adding a fixed 100 slots each time?
- When should you pass an initialCapacity to ArrayList(initialCapacity) in Kotlin, and what happens if you over-allocate?

## dsa/strings/text-building-cost
problems:
- [both] LeetCode 1108 | Defanging an IP Address | Easy | https://leetcode.com/problems/defanging-an-ip-address/ | Build a transformed string in a single O(n) pass using `StringBuilder`
- [both] LeetCode 151 | Reverse Words in a String | Medium | https://leetcode.com/problems/reverse-words-in-a-string/ | Parse words and join them in reverse order with O(n) total string-building work
- [both] LeetCode 394 | Decode String | Medium | https://leetcode.com/problems/decode-string/ | Repeatedly expand nested `[k[encoded]]` patterns without quadratic string copying
- [both] LeetCode 68 | Text Justification | Hard | https://leetcode.com/problems/text-justification/ | Construct padded lines to exact width using precalculated spacing in O(n) time
- [A] LeetCode 1768 | Merge Strings Alternately | Easy | https://leetcode.com/problems/merge-strings-alternately/ | Pre-size a character buffer and interleave two strings without intermediate allocations
- [A] LeetCode 6 | Zigzag Conversion | Medium | https://leetcode.com/problems/zigzag-conversion/ | Accumulate characters into row-specific `StringBuilder` buffers and merge once at the end
- [B] LeetCode 344 | Reverse String | Easy | https://leetcode.com/problems/reverse-string/ | Mutating a mutable CharArray in-place with O(1) extra memory
interview-questions:
- Why does `result += char` inside a loop of length n allocate O(n^2) total characters in Kotlin/Java?
- How does `StringBuilder` avoid creating a new `String` object on every append?
- Why are `String` objects immutable on the JVM, and what benefits does immutability give to security and hashing?
- Under the hood, how does Kotlin compile a single string template like `"User $name has $count items"` versus concatenation inside a loop?
- Why are Strings immutable in Kotlin and Java, and how does that make `str += char` inside a loop O(n^2)?
- How does StringBuilder avoid copying the entire string on every append, and how is it similar to ArrayList?
- When does the Kotlin compiler optimize string templates (`"$a$b"`) into a single StringBuilder or invokedynamic call, and when does it fail (such as across loop iterations)?
- What is the String Pool (interning) on the JVM/ART, and why would mutable strings break it?

## dsa/strings/compare-text-by-characters
problems:
- [both] LeetCode 242 | Valid Anagram | Easy | https://leetcode.com/problems/valid-anagram/ | Use a 26-slot `IntArray` frequency counter to compare two words in O(n) time and O(1) space
- [both] LeetCode 383 | Ransom Note | Easy | https://leetcode.com/problems/ransom-note/ | Verify character availability by decrementing frequency counts in one pass
- [both] LeetCode 387 | First Unique Character in a String | Easy | https://leetcode.com/problems/first-unique-character-in-a-string/ | Two-pass character frequency counting to find the first count of 1
- [both] LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | Bucket strings by their character-count signature without sorting each word
- [both] LeetCode 438 | Find All Anagrams in a String | Medium | https://leetcode.com/problems/find-all-anagrams-in-a-string/ | Maintain a sliding 26-character frequency window to match anagrams in O(n)
- [A] LeetCode 76 | Minimum Window Substring | Hard | https://leetcode.com/problems/minimum-window-substring/ | Track satisfied character counts across a dynamic window in O(n) time
- [B] HackerRank | Sherlock and Anagrams | Medium | https://www.hackerrank.com/challenges/sherlock-and-anagrams/problem | [Algorithms > Strings] Counts unordered anagrammatic substring pairs using frequency signatures
interview-questions:
- Why is counting characters in an `IntArray(26)` faster than sorting both strings in O(n log n)?
- When must you switch from a fixed `IntArray(26)` to a `HashMap<Char, Int>` when processing user text on Android?
- How does UTF-16 surrogate-pair representation affect `String.length` when a string contains emojis?
- How can a single 32-bit `Int` bitmask check whether two lowercase ASCII words share any common letters?
- When comparing whether two strings are anagrams, when should you use an IntArray(26) versus a HashMap<Char, Int>?
- How does Unicode (emojis, surrogate pairs, combining accents) complicate character-by-character comparison in Kotlin?
- Why is sorting two strings of length n to check for anagrams O(n log n) while counting characters is O(n) time and O(1) space for ASCII?
- How can you represent the set of lowercase ASCII letters present in a word using a single 32-bit Int bitmask?

## dsa/linked-lists/what-is-a-linked-list
problems:
- [both] LeetCode 876 | Middle of the Linked List | Easy | https://leetcode.com/problems/middle-of-the-linked-list/ | Use slow and fast pointers to find the midpoint in one traversal without knowing length
- [both] LeetCode 141 | Linked List Cycle | Easy | https://leetcode.com/problems/linked-list-cycle/ | Detect whether a chain of `next` pointers loops back on itself in O(1) space
- [both] LeetCode 142 | Linked List Cycle II | Medium | https://leetcode.com/problems/linked-list-cycle-ii/ | Prove and locate the exact node where a cycle begins using pointer speeds
- [both] LeetCode 138 | Copy List with Random Pointer | Medium | https://leetcode.com/problems/copy-list-with-random-pointer/ | Deep-copy heap-allocated nodes that hold both sequential and arbitrary references
- [A] LeetCode 1290 | Convert Binary Number in a Linked List to Integer | Easy | https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/ | Walk a singly linked list from `head` to `null` while accumulating state
- [A] LeetCode 160 | Intersection of Two Linked Lists | Easy | https://leetcode.com/problems/intersection-of-two-linked-lists/ | Synchronize two pointer traversals to find where two node chains merge in memory
- [B] HackerRank | Print the Elements of a Linked List | Easy | https://www.hackerrank.com/challenges/print-the-elements-of-a-linked-list/problem | [Data Structures > Linked Lists] Basic pointer traversal from head until null
- [B] LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Rewiring next pointers iteratively and recursively without losing the rest of the chain
interview-questions:
- Why does finding the 500th element of a linked list take O(n) steps while an array takes O(1)?
- How much extra memory overhead does each node in a doubly linked list carry compared to a primitive array element on a 64-bit JVM?
- Why are linked list traversals often slower than array traversals even when both are O(n)?
- How does Floyd's tortoise-and-hare algorithm guarantee that the fast pointer won't just jump over the slow pointer without meeting?
- Why does finding the k-th element in a linked list take O(k) time even though you know the exact index?
- How does Floyd's fast-and-slow pointer algorithm guarantee that the two pointers will meet inside a cycle rather than jumping over each other?
- Why do linked list nodes have poor CPU cache locality compared to an array?

## dsa/linked-lists/insert-and-remove-nodes
problems:
- [both] LeetCode 203 | Remove Linked List Elements | Easy | https://leetcode.com/problems/remove-linked-list-elements/ | Use a dummy sentinel node to handle deleting the head node cleanly
- [both] LeetCode 237 | Delete Node in a Linked List | Medium | https://leetcode.com/problems/delete-node-in-a-linked-list/ | Bypass a node when you aren't given access to the `head` pointer
- [both] LeetCode 19 | Remove Nth Node From End of List | Medium | https://leetcode.com/problems/remove-nth-node-from-end-of-list/ | Maintain a k-step gap between two pointers to unlink a target node in one pass
- [both] LeetCode 25 | Reverse Nodes in k-Group | Hard | https://leetcode.com/problems/reverse-nodes-in-k-group/ | Combine group counting, in-place sub-list reversal, and tail reconnection
- [A] LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Master rewiring `next` pointers iteratively and recursively without losing the rest of the list
- [A] LeetCode 24 | Swap Nodes in Pairs | Medium | https://leetcode.com/problems/swap-nodes-in-pairs/ | Re-link adjacent pairs of nodes carefully without creating a self-loop
- [B] HackerRank | Insert a node at a specific position in a linked list | Easy | https://www.hackerrank.com/challenges/insert-a-node-at-a-specific-position-in-a-linked-list/problem | [Data Structures > Linked Lists] Splicing a new node in without losing the tail reference
- [B] LeetCode 707 | Design Linked List | Medium | https://leetcode.com/problems/design-linked-list/ | Implementing head, tail, and index-based insertion and deletion from scratch
interview-questions:
- What problem does a dummy (`sentinel`) head node solve when inserting or deleting in a singly linked list?
- In what exact order must you update pointers when inserting a new node between `prev` and `curr`?
- Why can you delete a node in a doubly linked list in O(1) given only a reference to that node, whereas a singly linked list normally needs the predecessor?
- What happens in garbage-collected languages like Kotlin if you unlink a node from a list, versus if a stagnant reference still points to it?
- What is the "dummy head" (sentinel node) pattern, and which edge cases does it eliminate when deleting nodes?
- In what order must you assign pointers when inserting newNode between prev and prev.next, and what breaks if you reverse those two lines?
- Why is deleting a node given a reference to it O(1) in a doubly linked list, but O(n) in a singly linked list (unless you copy values)?
- Why does the copy-next-node's-value trick (LeetCode 237) fail if the node to delete is the tail node?

## dsa/linked-lists/linked-list-vs-arraylist
problems:
- [both] LeetCode 143 | Reorder List | Medium | https://leetcode.com/problems/reorder-list/ | Experience why O(1)-space linked list interleaving requires finding the middle, reversing, and merging
- [both] LeetCode 1472 | Design Browser History | Medium | https://leetcode.com/problems/design-browser-history/ | Compare a doubly linked list against a dynamic array with a cursor for `visit`, `back`, and `forward`
- [both] LeetCode 146 | LRU Cache | Medium | https://leetcode.com/problems/lru-cache/ | See the textbook case where a doubly linked list + HashMap beats an `ArrayList` with O(1) middle removal
- [A] LeetCode 707 | Design Linked List | Medium | https://leetcode.com/problems/design-linked-list/ | Implement `get`, `addAtHead`, `addAtTail`, `addAtIndex`, and `deleteAtIndex` to feel every cost directly
- [A] LeetCode 2 | Add Two Numbers | Medium | https://leetcode.com/problems/add-two-numbers/ | Stream node-by-node creation where output length isn't fixed ahead of time
- [B] LeetCode 21 | Merge Two Sorted Lists | Easy | https://leetcode.com/problems/merge-two-sorted-lists/ | O(1) extra space splicing of existing nodes vs allocating a new merged array
- [B] LeetCode 234 | Palindrome Linked List | Easy | https://leetcode.com/problems/palindrome-linked-list/ | Shows the extra work required (find middle + reverse half) when O(1) random access is missing
- [B] LeetCode 460 | LFU Cache | Hard | https://leetcode.com/problems/lfu-cache/ | Combines frequency buckets of doubly linked lists for strict O(1) eviction and promotion
interview-questions:
- Why does Kotlin's standard library default `mutableListOf()` to `ArrayList` and almost never recommend `LinkedList`?
- If inserting in the middle of a `LinkedList` is O(1) once you are at the node, why is `linkedList.add(index, element)` still O(n)?
- In `LRU Cache`, why does a doubly linked list beat an `ArrayList` when promoting a recently accessed item to the front?
- Why did Kotlin introduce `ArrayDeque` instead of recommending `LinkedList` for queue and stack operations?
- If LinkedList has O(1) insertion and deletion, why is ArrayList almost always faster in practice even for workloads that insert in the middle?
- Name two real-world data structures where a doubly linked list genuinely beats an ArrayList.
- Why is ArrayDeque preferred over LinkedList when implementing a Queue or Stack in Kotlin/Java?
- What happens to garbage collection pressure on Android when you frequently add and remove items from a LinkedList vs an ArrayList?

## dsa/stacks/what-is-a-stack
problems:
- [both] LeetCode 155 | Min Stack | Medium | https://leetcode.com/problems/min-stack/ | Augment a stack with running minimums so `push`, `pop`, `top`, and `getMin` are all O(1)
- [both] LeetCode 150 | Evaluate Reverse Polish Notation | Medium | https://leetcode.com/problems/evaluate-reverse-polish-notation/ | Evaluate postfix arithmetic expressions using an operand stack
- [both] LeetCode 739 | Daily Temperatures | Medium | https://leetcode.com/problems/daily-temperatures/ | Maintain a monotonic decreasing stack to find the next warmer day in O(n)
- [A] LeetCode 682 | Baseball Game | Easy | https://leetcode.com/problems/baseball-game/ | Push scores and pop/peek previous entries to model undo and history operations
- [A] LeetCode 1047 | Remove All Adjacent Duplicates In String | Easy | https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/ | Use the top of a stack to cancel adjacent matching characters in O(n)
- [A] LeetCode 232 | Implement Queue using Stacks | Easy | https://leetcode.com/problems/implement-queue-using-stacks/ | Pour elements between two LIFO stacks to reverse order into FIFO in amortized O(1)
- [B] HackerRank | Maximum Element | Easy | https://www.hackerrank.com/challenges/maximum-element/problem | [Data Structures > Stacks] Tracks LIFO pushes, pops, and running maximums
- [B] LeetCode 225 | Implement Stack using Queues | Easy | https://leetcode.com/problems/implement-stack-using-queues/ | Contrasts LIFO and FIFO ordering by building one from the other
- [B] LeetCode 42 | Trapping Rain Water | Hard | https://leetcode.com/problems/trapping-rain-water/ | Uses a monotonic stack to bound horizontal water layers between taller bars
interview-questions:
- How does the Android Activity back stack use LIFO ordering when a user presses the Back button?
- Why should you use `ArrayDeque` instead of `java.util.Stack` in Kotlin and Java?
- How can `Min Stack` support `getMin()` in O(1) time even after the minimum element is popped?
- What is a monotonic stack, and what kind of "next greater element" problems does it reduce from O(n^2) to O(n)?
- How does an Android activity back stack or Jetpack Navigation back stack behave as a LIFO structure?
- Why should you use Kotlin's ArrayDeque instead of java.util.Stack when you need a stack?
- How can a stack maintain its minimum (or maximum) element in O(1) time without scanning the stack on pop()?
- What is a monotonic stack, and what kind of array problem ("next greater element") does it solve in O(n) time?

## dsa/stacks/matching-brackets
problems:
- [both] LeetCode 20 | Valid Parentheses | Easy | https://leetcode.com/problems/valid-parentheses/ | Push opening brackets and verify that every closing bracket matches the top of the stack
- [both] HackerRank (Data Structures > Stacks) | Balanced Brackets | Medium | https://www.hackerrank.com/challenges/balanced-brackets/problem | Handle edge cases like unmatched closing brackets and leftover open brackets at the end
- [both] LeetCode 921 | Minimum Add to Make Parentheses Valid | Medium | https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/ | Count unmatched opening and closing brackets to repair an invalid string
- [both] LeetCode 1249 | Minimum Remove to Make Valid Parentheses | Medium | https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/ | Push character indices onto a stack so you know exactly which invalid brackets to delete
- [both] LeetCode 32 | Longest Valid Parentheses | Hard | https://leetcode.com/problems/longest-valid-parentheses/ | Use a stack of indices with a base sentinel to measure the longest balanced substring in O(n)
- [A] LeetCode 1614 | Maximum Nesting Depth of the Parentheses | Easy | https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/ | Track current stack depth to measure maximum nesting level
- [B] LeetCode 856 | Score of Parentheses | Medium | https://leetcode.com/problems/score-of-parentheses/ | Uses stack depth to accumulate nested parenthesis scores
interview-questions:
- Why can't you validate nested brackets of three types `()`, `[]`, `{}` simply by counting +1 for open and -1 for close?
- For a single bracket type `()`, how can you check validity in O(1) space without allocating a stack?
- What two edge cases cause a naive bracket-matching solution to crash with `NoSuchElementException` or return `true` on `"((("`?
- Why is pushing the *index* of an opening bracket onto the stack often more useful than pushing the character itself?
- Why can you validate a string with only one bracket type `(` and `)` using a single integer counter in O(1) space, whereas `([{` requires an O(n) stack?
- What two edge cases cause a naive bracket-matching implementation to crash or return true on invalid strings like `")("` or `"((("`?
- How is XML/HTML tag matching in an Android layout parser related to the balanced-brackets stack problem?
- How do you modify the stack in Valid Parentheses if you need to report the exact character index where the first mismatch occurred?

## dsa/queues/what-is-a-queue
problems:
- [both] LeetCode 622 | Design Circular Queue | Medium | https://leetcode.com/problems/design-circular-queue/ | Implement `head` and `tail` wrap-around indices on a fixed-size array with `(index + 1) % capacity`
- [both] LeetCode 950 | Reveal Cards In Increasing Order | Medium | https://leetcode.com/problems/reveal-cards-in-increasing-order/ | Simulate deck-dealing rules backward using a queue of indices
- [both] LeetCode 649 | Dota2 Senate | Medium | https://leetcode.com/problems/dota2-senate/ | Use two FIFO queues storing round indices to simulate cyclic turn-based elimination
- [A] LeetCode 225 | Implement Stack using Queues | Easy | https://leetcode.com/problems/implement-stack-using-queues/ | Rotate a FIFO queue on push or pop to simulate LIFO behavior
- [A] LeetCode 2073 | Time Needed to Buy Tickets | Easy | https://leetcode.com/problems/time-needed-to-buy-tickets/ | Model round-robin FIFO service where people re-enter the back of the line
- [B] LeetCode 232 | Implement Queue using Stacks | Easy | https://leetcode.com/problems/implement-queue-using-stacks/ | Amortized O(1) FIFO queue built by reversing LIFO order across two stacks
- [B] HackerRank | Queue using Two Stacks | Medium | https://www.hackerrank.com/challenges/queue-using-two-stacks/problem | [Data Structures > Queues] Processes enqueue, dequeue, and peek queries in strict arrival order
interview-questions:
- How does a circular buffer (`ArrayDeque`) achieve O(1) `addLast` and `removeFirst` on a plain array without shifting elements?
- Where are FIFO queues used inside Android's `Looper` and `MessageQueue` architecture?
- When using `head` and `tail` indices in a circular array, how do you distinguish a completely full queue from a completely empty queue?
- What is the difference between a standard Queue, a Deque (double-ended queue), and a PriorityQueue?
- How does a circular buffer (ring array) allow ArrayDeque to enqueue at the back and dequeue from the front in O(1) time without shifting elements?
- In Android, how does the main thread's MessageQueue order UI events and Runnables posted via a Handler?
- Why is implementing a queue with two stacks still O(1) amortized per operation even though one pop() may move n elements?
- In a ring buffer where head == tail can mean either completely empty or completely full, how do you distinguish the two states?

## dsa/queues/keeping-the-last-n-items
problems:
- [both] LeetCode 933 | Number of Recent Calls | Easy | https://leetcode.com/problems/number-of-recent-calls/ | Push new timestamps and evict anything older than `t - 3000` from the front of the queue
- [both] LeetCode 346 | Moving Average from Data Stream | Easy | https://leetcode.com/problems/moving-average-from-data-stream/ | Maintain a fixed-capacity queue of the last k values and update a running sum in O(1)
- [both] LeetCode 641 | Design Circular Deque | Medium | https://leetcode.com/problems/design-circular-deque/ | Support bounded insertions and evictions at both ends of a ring buffer
- [both] LeetCode 362 | Design Hit Counter | Medium | https://leetcode.com/problems/design-hit-counter/ | Aggregate and evict timestamped hits over a rolling 300-second window
- [both] LeetCode 239 | Sliding Window Maximum | Hard | https://leetcode.com/problems/sliding-window-maximum/ | Keep a monotonic deque of candidate indices to query the max of the last k items in O(1)
interview-questions:
- If you want to keep only the latest 100 log lines in an Android diagnostic buffer, why is a circular array better than a `MutableList`?
- How do you maintain the average of the last N stream values in O(1) time per item without summing the queue every time?
- In `Design Hit Counter`, what breaks if 10,000 requests arrive at the exact same millisecond and you store every timestamp individually?
- How does a monotonic deque keep track of the maximum in a sliding window when the old maximum ages out of the window?
- If you want to keep an in-memory crash-log buffer of only the last 100 log lines in an Android app, why is a fixed-size circular array better than a LinkedList?
- How do you update the running average of the last N items in O(1) time when the (N+1)-th item arrives?
- What happens if 10,000 events arrive in the same millisecond in a timestamp-based queue (like Hit Counter), and how do circular time-buckets bound the memory to O(window)?
- Why does Sliding Window Maximum use a double-ended queue (Deque) instead of a regular FIFO Queue or PriorityQueue?

## dsa/hashing/hashing-idea
problems:
- [both] LeetCode 217 | Contains Duplicate | Easy | https://leetcode.com/problems/contains-duplicate/ | Replace O(n^2) pairwise checks with O(1) average-time `HashSet` lookups
- [both] LeetCode 1 | Two Sum | Easy | https://leetcode.com/problems/two-sum/ | Map values to indices for instant complement lookup in a single pass
- [both] HackerRank (Data Structures > Dictionaries and Hashmaps) | Frequency Queries | Medium | https://www.hackerrank.com/challenges/frequency-queries/problem | Maintain two synchronized hash maps (value-to-count and count-to-frequency) for O(1) queries
- [both] LeetCode 128 | Longest Consecutive Sequence | Medium | https://leetcode.com/problems/longest-consecutive-sequence/ | Use a `HashSet` to start sequence scans only at sequence heads, reducing O(n log n) to O(n)
- [A] LeetCode 349 | Intersection of Two Arrays | Easy | https://leetcode.com/problems/intersection-of-two-arrays/ | Convert one list into a set to filter another list in O(n + m) time
- [A] LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Map each element to its array index so removal by value is O(1)
- [B] HackerRank | Ransom Note | Easy | https://www.hackerrank.com/challenges/ctci-ransom-note/problem | [Interview Preparation Kit > Dictionaries and Hashmaps] Word-level frequency map lookup in O(1) per word
- [B] LeetCode 359 | Logger Rate Limiter | Easy | https://leetcode.com/problems/logger-rate-limiter/ | Maps each message string to its last printed timestamp for O(1) deduplication
interview-questions:
- How does a hash table convert an arbitrary key like `"user_42"` into an array bucket index?
- What is the load factor of a `HashMap`, and why does Java/Kotlin resize the table when it reaches 0.75?
- Why is iterating over a `HashSet` unordered, and how does `LinkedHashSet` preserve insertion order?
- Why does resizing a `HashMap` require recomputing bucket positions for every existing entry?
- Walk through the exact steps a HashMap takes from receiving a key object to finding its value in the underlying bucket array.
- Why does a HashMap allocate a power-of-two array size in Java/Kotlin, and how does `(n - 1) & hash` replace the slower modulo `%` operator?
- What is the load factor of a HashMap (0.75 by default on the JVM), and what happens when the map exceeds it?
- On Android, when would you consider SparseArray or ArrayMap instead of HashMap, and at what size do they become slower than HashMap?

## dsa/hashing/collisions
problems:
- [both] LeetCode 705 | Design HashSet | Easy | https://leetcode.com/problems/design-hashset/ | Build a hash set from scratch using an array of buckets and separate chaining for collisions
- [both] LeetCode 706 | Design HashMap | Easy | https://leetcode.com/problems/design-hashmap/ | Store `(key, value)` nodes in collision chains and handle key updates vs new insertions
- [both] LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | intentionally design a canonical hash key so all anagrams collide into the same group
- [both] LeetCode 535 | Encode and Decode TinyURL | Medium | https://leetcode.com/problems/encode-and-decode-tinyurl/ | Handle short-key generation and collision resolution in a bi-directional map
- [A] LeetCode 205 | Isomorphic Strings | Easy | https://leetcode.com/problems/isomorphic-strings/ | Prevent two distinct characters from colliding into the same mapped target character
- [B] LeetCode 249 | Group Shifted Strings | Medium | https://leetcode.com/problems/group-shifted-strings/ | Designs a custom shift-invariant hash key to bucket equivalent strings together
interview-questions:
- What is the difference between separate chaining and open addressing (linear probing) when two keys land in the same bucket?
- Why is a `HashMap` lookup O(1) on average but O(n) in the worst case, and how did Java 8 mitigate this with balanced trees (`TreeNode`)?
- In open addressing with linear probing, why can't you simply set a bucket to `null` when deleting an entry?
- What is a hash flooding Denial-of-Service attack, and how can attackers exploit predictable hash functions?
- Compare separate chaining (linked lists / balanced trees in buckets) with open addressing (linear probing) when two keys hash to the same bucket.
- Why is a HashMap lookup O(1) on average, O(n) in the classic worst case, and O(log n) in modern Java/Kotlin when keys are Comparable?
- What is a hash-collision Denial-of-Service (HashDoS) attack, and how can an attacker craft JSON keys that freeze a server or app?
- Why does Java's HashMap XOR the upper 16 bits of `hashCode()` with the lower 16 bits (`h ^ (h >>> 16)`) before picking a bucket?

## dsa/hashing/hashcode-and-equals-contract
problems:
- [both] LeetCode 290 | Word Pattern | Easy | https://leetcode.com/problems/word-pattern/ | Distinguish reference identity from structural `equals()` when mapping `Char` and `String` keys
- [both] LeetCode 149 | Max Points on a Line | Hard | https://leetcode.com/problems/max-points-on-a-line/ | Represent reduced slope fractions `(dy, dx)` as a custom key with proper `equals` and `hashCode` instead of lossy `Double`
- [A] LeetCode 706 | Design HashMap | Easy | https://leetcode.com/problems/design-hashmap/ | See firsthand how bucket index uses `hashCode()` while bucket chain traversal uses `equals()`
- [A] LeetCode 219 | Contains Duplicate II | Easy | https://leetcode.com/problems/contains-duplicate-ii/ | Rely on value equality inside a map to track the most recent index of an equal key
- [A] LeetCode 49 | Group Anagrams | Medium | https://leetcode.com/problems/group-anagrams/ | Discover why `IntArray` fails as a `HashMap` key in Kotlin/Java (reference `hashCode`) while `String` or `List<Int>` works
- [B] LeetCode 349 | Intersection of Two Arrays | Easy | https://leetcode.com/problems/intersection-of-two-arrays/ | Relies on value equality and hashing to deduplicate elements across sets
- [B] LeetCode 205 | Isomorphic Strings | Easy | https://leetcode.com/problems/isomorphic-strings/ | Bi-directional mapping where key equality and uniqueness must hold in both directions
- [B] LeetCode 36 | Valid Sudoku | Medium | https://leetcode.com/problems/valid-sudoku/ | Encodes composite `(row, col, value)` tuples with proper equality and hashing in a single Set
interview-questions:
- What are the exact rules of the `equals()` and `hashCode()` contract on the JVM?
- What happens when you put an object into a `HashSet`, mutate one of the fields used in its `hashCode()`, and then call `set.contains(obj)`?
- Why does using a `ByteArray` or `IntArray` inside a Kotlin `data class` break value equality in a `HashMap`, and how do you fix it?
- If two objects have the same `hashCode()`, must `equals()` return `true`? What if `equals()` returns `true`—must their `hashCode()` be identical?
- State the exact contract between `equals()` and `hashCode()`. What breaks in a HashSet if two objects are `equals() == true` but return different `hashCode()` values?
- If two objects return the exact same `hashCode()`, must `a.equals(b)` return true? What happens if every object returns `hashCode() = 42`?
- Why does putting a Kotlin `data class` with a `var` property into a `HashSet` and then mutating that `var` make the object impossible to find or remove?
- In Kotlin, why does a `data class` containing a `ByteArray` or `IntArray` break map lookups unless you override `equals()` and `hashCode()` with `contentEquals()` and `contentHashCode()`?

## dsa/recursion/what-is-recursion
problems:
- [both] LeetCode 231 | Power of Two | Easy | https://leetcode.com/problems/power-of-two/ | Recursively divide n by 2 until reaching base case `n == 1` or an odd number
- [both] LeetCode 21 | Merge Two Sorted Lists | Easy | https://leetcode.com/problems/merge-two-sorted-lists/ | Define list merging recursively: pick the smaller head and set its `next` to the recursive merge
- [both] LeetCode 50 | Pow(x, n) | Medium | https://leetcode.com/problems/powx-n/ | Shrink problem size by half on every recursive call (`n / 2`)
- [both] LeetCode 779 | K-th Symbol in Grammar | Medium | https://leetcode.com/problems/k-th-symbol-in-grammar/ | Trace a value in row n back to its parent in row n - 1 without building the rows
- [A] LeetCode 344 | Reverse String | Easy | https://leetcode.com/problems/reverse-string/ | Identify the base case (`left >= right`) and the recursive step that shrinks the problem by 2 elements
- [A] LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Contrast iterative pointer reversal with recursive reversal on the return path
- [B] LeetCode 509 | Fibonacci Number | Easy | https://leetcode.com/problems/fibonacci-number/ | Simplest two-branch recursive definition with base cases n = 0 and n = 1
- [B] HackerRank | Recursive Digit Sum | Medium | https://www.hackerrank.com/challenges/recursive-digit-sum/problem | [Algorithms > Recursion] Reduces a huge number to its super digit by recursing until a single-digit base case
interview-questions:
- What two parts must every recursive function have so it does not run forever?
- How do you prove that a recursive function terminates for all valid inputs?
- What is tail recursion, and what does the `tailrec` modifier in Kotlin actually compile down to in bytecode?
- When is a recursive solution cleaner to read than an iterative loop, and when is it dangerous in production?
- What two parts must every terminating recursive function have, and what happens at runtime if either is wrong?
- How do you prove by mathematical induction that a recursive function works for every input size n?
- When is a recursive solution clearer than an iterative loop, and when is an iterative loop safer on the JVM/Android?
- What is Kotlin's `tailrec` modifier, what requirement must the recursive call satisfy to use it, and what bytecode does the compiler generate?

## dsa/recursion/the-call-stack
problems:
- [both] LeetCode 104 | Maximum Depth of Binary Tree | Easy | https://leetcode.com/problems/maximum-depth-of-binary-tree/ | See how maximum recursion call-stack depth directly matches the height of the tree
- [both] LeetCode 394 | Decode String | Medium | https://leetcode.com/problems/decode-string/ | Compare saving local variables on the recursive call stack vs pushing state onto an explicit stack
- [both] LeetCode 341 | Flatten Nested List Iterator | Medium | https://leetcode.com/problems/flatten-nested-list-iterator/ | Unroll recursive tree traversal into a lazy on-demand iterator using an explicit stack of frames
- [A] LeetCode 94 | Binary Tree Inorder Traversal | Easy | https://leetcode.com/problems/binary-tree-inorder-traversal/ | Replace the JVM call stack with an explicit `ArrayDeque` stack to see what frames store
- [A] LeetCode 114 | Flatten Binary Tree to Linked List | Medium | https://leetcode.com/problems/flatten-binary-tree-to-linked-list/ | Compare O(h) call-stack recursion against O(1)-space Morris-style pointer threading
- [B] LeetCode 206 | Reverse Linked List | Easy | https://leetcode.com/problems/reverse-linked-list/ | Compare the O(n) call-stack space of the recursive version with the O(1) space of the iterative version
- [B] LeetCode 636 | Exclusive Time of Functions | Medium | https://leetcode.com/problems/exclusive-time-of-functions/ | Literally simulates a single-threaded CPU call stack pushing on `"start"` and popping on `"end"`
interview-questions:
- What exact data is stored inside a single stack frame when a function calls another function?
- What causes a `StackOverflowError` on the JVM, and roughly how deep can standard recursion go before crashing?
- Why is the space complexity of recursive DFS on a binary tree O(h) rather than O(n), and when does O(h) become O(n)?
- How can you convert any recursive function that risks `StackOverflowError` into an iterative function?
- What exact information is stored inside a single stack frame when function A calls function B?
- Why does deep recursion throw `StackOverflowError` on Android before exhausting the device's RAM, and roughly how deep can a JVM/ART call stack go?
- How do you convert any recursive function into an iterative function that cannot throw `StackOverflowError`?
- Why does the recursive solution to `Maximum Depth of Binary Tree` use O(n) space in the worst case even though you never allocate a list or array?

## dsa/recursion/repeated-work-in-recursion
problems:
- [both] LeetCode 70 | Climbing Stairs | Easy | https://leetcode.com/problems/climbing-stairs/ | Map a counting problem to the Fibonacci recurrence and eliminate duplicate subtrees
- [both] LeetCode 1137 | N-th Tribonacci Number | Easy | https://leetcode.com/problems/n-th-tribonacci-number/ | See O(3^n) branching collapse to O(n) when repeated states are saved
- [both] LeetCode 95 | Unique Binary Search Trees II | Medium | https://leetcode.com/problems/unique-binary-search-trees-ii/ | Reuse recursively generated left and right subtrees across combinations
- [both] LeetCode 894 | All Possible Full Binary Trees | Medium | https://leetcode.com/problems/all-possible-full-binary-trees/ | Cache lists of trees for smaller `n` so recursive subproblems aren't rebuilt repeatedly
- [A] LeetCode 509 | Fibonacci Number | Easy | https://leetcode.com/problems/fibonacci-number/ | Observe how naive `fib(n-1) + fib(n-2)` explodes to O(2^n) calls and collapses to O(n) with a cache
- [B] LeetCode 746 | Min Cost Climbing Stairs | Easy | https://leetcode.com/problems/min-cost-climbing-stairs/ | Overlapping recursive subproblems on step costs
- [B] LeetCode 140 | Word Break II | Hard | https://leetcode.com/problems/word-break-ii/ | Without memoizing suffix results, pathological inputs force exponential re-exploration of the same suffix
interview-questions:
- Why does naive recursive `fib(50)` make billions of function calls when there are only 51 distinct values of `fib(0)..fib(50)`?
- How do you draw a recursion tree to spot whether a recursive algorithm has overlapping subproblems?
- Why doesn't Merge Sort suffer from repeated work even though it makes two recursive calls just like Fibonacci?
- How much time and space does adding a `HashMap` or array cache to naive recursive Fibonacci take?
- Draw or describe the recursion tree for naive `fib(5)`. How many times is `fib(2)` computed, and why does the total call count grow as O(2^n)?
- Why does Merge Sort make two recursive calls per step and run in O(n log n) time, while naive Fibonacci makes two recursive calls per step and runs in O(2^n) time?
- How can you tell by looking at the arguments of a recursive function how many unique states can ever be called?
- What is the space cost of adding a memoization array or map to eliminate repeated work in recursion?

## dsa/sorting/what-sorting-means
problems:
- [both] LeetCode 1122 | Relative Sort Array | Easy | https://leetcode.com/problems/relative-sort-array/ | Sort items according to a custom rank order with ties broken by natural value
- [both] LeetCode 937 | Reorder Data in Log Files | Medium | https://leetcode.com/problems/reorder-data-in-log-files/ | Apply stability so digit-logs stay in their original relative order while letter-logs sort by content then identifier
- [both] LeetCode 179 | Largest Number | Medium | https://leetcode.com/problems/largest-number/ | Define a transitive comparator `(b + a).compareTo(a + b)` to order strings for maximum concatenation
- [A] LeetCode 905 | Sort Array By Parity | Easy | https://leetcode.com/problems/sort-array-by-parity/ | Partition elements by a binary key (even vs odd) and observe whether relative order is preserved
- [A] LeetCode 791 | Custom Sort String | Medium | https://leetcode.com/problems/custom-sort-string/ | Define a custom total ordering over characters and sort in O(n)
- [B] LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Sorts items with many duplicate keys, highlighting what happens to tied elements
- [B] LeetCode 1366 | Rank Teams by Votes | Medium | https://leetcode.com/problems/rank-teams-by-votes/ | Multi-key sorting across 26 tie-breaking positions with alphabetical fallback
interview-questions:
- What does it mean for a sorting algorithm to be *stable*, and give a UI example where an unstable sort breaks user expectations?
- Which classic sorting algorithms are stable (Merge Sort, Insertion Sort) and which are unstable (Quick Sort, Heap Sort)?
- What are the three mathematical rules (reflexivity, antisymmetry, transitivity) that a custom `Comparator` must obey so `sortWith` doesn't throw `IllegalArgumentException`?
- How do you sort a list of employees by department ascending, and for ties, by salary descending in Kotlin?
- What does it mean for a sorting algorithm to be **stable**, and why does stability matter when a user sorts a table by "Name" and then clicks "Sort by Department"?
- Which of Bubble Sort, Insertion Sort, Selection Sort, Merge Sort, Quick Sort, and Heap Sort are naturally stable?
- What mathematical properties (reflexivity, antisymmetry, transitivity) must a custom `Comparator` satisfy so `List.sortedWith()` does not throw `IllegalArgumentException: Comparison method violates its general contract!`?
- What is the difference between an in-place sort and an out-of-place sort?

## dsa/sorting/simple-sorts
problems:
- [both] HackerRank (Algorithms > Sorting) | Insertion Sort - Part 1 | Easy | https://www.hackerrank.com/challenges/insertionsort1/problem | Shift larger elements right to insert a single out-of-order value into its sorted slot
- [both] HackerRank (Algorithms > Sorting) | Insertion Sort - Part 2 | Easy | https://www.hackerrank.com/challenges/insertionsort2/problem | Run full Insertion Sort step-by-step and watch the sorted prefix grow
- [both] LeetCode 147 | Insertion Sort List | Medium | https://leetcode.com/problems/insertion-sort-list/ | Implement Insertion Sort on a singly linked list without array indexing
- [A] HackerRank (Algorithms > Sorting) | Running Time of Algorithms | Easy | https://www.hackerrank.com/challenges/runningtime/problem | Count the exact number of shifts Insertion Sort makes and relate it to inversions
- [A] LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Improve on simple swapping with Dutch National Flag 3-way partitioning in one pass
- [B] HackerRank | Bubble Sort | Easy | https://www.hackerrank.com/challenges/ctci-bubble-sort/problem | [Interview Preparation Kit > Sorting] Implements neighbour-swapping Bubble Sort and counts total swaps
- [B] LeetCode 969 | Pancake Sorting | Medium | https://leetcode.com/problems/pancake-sorting/ | Selection-sort style logic: repeatedly bring the largest unsorted item to its final slot
interview-questions:
- Why does Insertion Sort run in O(n) time on an already-sorted or nearly-sorted array while Selection Sort still takes O(n^2)?
- Why do industrial sorting libraries like TimSort switch to Insertion Sort for small subarrays (typically n <= 32)?
- How many swaps does Selection Sort perform in the worst case compared to Bubble Sort?
- If an array has only `k` inversions (pairs out of order), what is the exact runtime of Insertion Sort?
- Why do production library sorts (like Timsort and Dual-Pivot Quicksort) switch to Insertion Sort when a subarray shrinks below ~32–47 elements?
- Which simple sort performs at most O(n) swaps even in the worst case, making it useful when writing to memory is much more expensive than reading?
- How can you modify Bubble Sort so that it terminates in O(n) time if the input array is already sorted?

## dsa/sorting/merge-sort
problems:
- [both] LeetCode 88 | Merge Sorted Array | Easy | https://leetcode.com/problems/merge-sorted-array/ | Master the two-pointer `merge` step that combines two sorted halves in O(n)
- [both] LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implement full top-down or bottom-up Merge Sort to guarantee O(n log n) worst-case time
- [both] LeetCode 148 | Sort List | Medium | https://leetcode.com/problems/sort-list/ | Sort a linked list in O(n log n) time using slow/fast midpoint splitting and merging
- [both] LeetCode 315 | Count of Smaller Numbers After Self | Hard | https://leetcode.com/problems/count-of-smaller-numbers-after-self/ | Piggyback on Merge Sort's merge step to count right-side smaller elements in O(n log n)
- [both] LeetCode 23 | Merge k Sorted Lists | Hard | https://leetcode.com/problems/merge-k-sorted-lists/ | Apply divide-and-conquer pairwise merging across k lists in O(N log k) time
- [A] LeetCode 21 | Merge Two Sorted Lists | Easy | https://leetcode.com/problems/merge-two-sorted-lists/ | Merge two sorted linked lists in O(1) auxiliary space
- [B] HackerRank | Merge Sort: Counting Inversions | Hard | https://www.hackerrank.com/challenges/ctci-merge-sort/problem | [Interview Preparation Kit > Sorting] Counts out-of-order pairs during the merge step in O(n log n)
interview-questions:
- Why does Merge Sort guarantee O(n log n) time even in the worst case, and what is its space trade-off on arrays?
- Why is Merge Sort the preferred algorithm for sorting linked lists over Quick Sort?
- How does bottom-up iterative Merge Sort avoid recursion stack frames altogether?
- How can you modify the `merge` step of Merge Sort to count the total number of inversions in an array in O(n log n)?
- Why does Merge Sort guarantee O(n log n) time in the best, average, and worst cases regardless of input order?
- Why does Merge Sort on an array require O(n) auxiliary space, while Merge Sort on a linked list can be done in O(1) auxiliary space (bottom-up)?
- During the `merge(left, right)` step, what slight condition (`<=` vs `<`) preserves or destroys the stability of Merge Sort?
- How is external Merge Sort used to sort a 100 GB log file when your machine only has 4 GB of RAM?

## dsa/sorting/quick-sort
problems:
- [both] HackerRank (Algorithms > Sorting) | Quicksort 1 - Partition | Easy | https://www.hackerrank.com/challenges/quicksort1/problem | Partition an array around a pivot into `less`, `equal`, and `greater` groups
- [both] LeetCode 75 | Sort Colors | Medium | https://leetcode.com/problems/sort-colors/ | Master 3-way Dutch National Flag partitioning to handle duplicate keys without O(n^2) degradation
- [both] LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implement Quick Sort with a randomized pivot and 3-way partition to pass all-duplicate test cases
- [both] LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Use Quickselect partitioning to recurse on only one side in O(n) average time
- [both] LeetCode 973 | K Closest Points to Origin | Medium | https://leetcode.com/problems/k-closest-points-to-origin/ | Partition points by squared Euclidean distance in O(n) average time without full sorting
interview-questions:
- What input causes naive Quick Sort (picking the first or last element as pivot) to degrade to O(n^2), and how do randomized pivot or median-of-three fix it?
- Why does standard 2-way Quick Sort degrade to O(n^2) when all elements in the array are identical, and how does 3-way partitioning solve this?
- Even though Quick Sort and Merge Sort are both O(n log n) on average, why is Quick Sort usually faster in practice for primitive arrays?
- What is the worst-case and best-case recursion stack space of Quick Sort, and how does tail-call recursion on the larger partition bound space to O(log n)?
- What input makes a naive Quick Sort (picking the first or last element as pivot) degrade to O(n^2) time and O(n) stack depth?
- How do randomized pivot selection (or median-of-three) and 3-way partitioning protect Quick Sort against sorted and all-duplicate inputs?
- If Merge Sort is guaranteed O(n log n) worst-case and Quick Sort is O(n^2) worst-case, why is Quick Sort often faster in practice on primitive arrays?
- Why is standard in-place Quick Sort unstable, and why do standard libraries use Dual-Pivot Quicksort for primitive arrays (`IntArray`) but Timsort for object arrays (`Array<T>`)?

## dsa/sorting/sorting-in-the-standard-library
problems:
- [both] LeetCode 1636 | Sort Array by Increasing Frequency | Easy | https://leetcode.com/problems/sort-array-by-increasing-frequency/ | Chain primary and secondary comparators (`compareBy` then `thenByDescending`)
- [both] LeetCode 56 | Merge Intervals | Medium | https://leetcode.com/problems/merge-intervals/ | Sort intervals by start time `sortedBy { it[0] }` as the O(n log n) preprocessing step
- [both] LeetCode 406 | Queue Reconstruction by Height | Medium | https://leetcode.com/problems/queue-reconstruction-by-height/ | Sort by height descending and k ascending using a composite comparator
- [both] LeetCode 354 | Russian Doll Envelopes | Hard | https://leetcode.com/problems/russian-doll-envelopes/ | Sort width ascending and height descending on ties so 2D nesting reduces to 1D LIS
- [A] LeetCode 2418 | Sort the People | Easy | https://leetcode.com/problems/sort-the-people/ | Pair indices or objects and sort using a descending key selector
- [B] HackerRank | Mark and Toys | Easy | https://www.hackerrank.com/challenges/mark-and-toys/problem | [Interview Preparation Kit > Sorting] Uses standard library sort to greedily buy the cheapest items first
interview-questions:
- In Kotlin, what is the memory and performance difference between `list.sortedBy { it.age }` and `mutableList.sortBy { it.age }`?
- Which sorting algorithms do `Arrays.sort(IntArray)` (Dual-Pivot Quicksort) and `Arrays.sort(Array<T>)` (TimSort) use on the JVM, and why do they differ?
- Why is TimSort able to sort real-world partially ordered data in as little as O(n) time?
- If your key function is expensive to compute, why should you avoid calling it repeatedly inside a `Comparator` without caching?
- In Kotlin, what is the difference between `list.sortedBy { ... }` and `mutableList.sortBy { ... }` in terms of time, memory allocation, and return type?
- What algorithm backs `Arrays.sort(Object[])` / `List.sorted()` on the JVM (Timsort), and why does it run in O(n) time on partially sorted real-world data?
- Why does `IntArray.sort()` use Dual-Pivot Quicksort while `List<Int>.sorted()` uses Timsort?
- If your selector inside `sortedBy { expensiveCalculation(it) }` is slow, how many times is `expensiveCalculation` called for n items, and how can you reduce it to exactly n calls?

## dsa/searching/binary-search
problems:
- [both] LeetCode 704 | Binary Search | Easy | https://leetcode.com/problems/binary-search/ | Implement classic exact-match binary search and use `low + (high - low) / 2`
- [both] LeetCode 374 | Guess Number Higher or Lower | Easy | https://leetcode.com/problems/guess-number-higher-or-lower/ | Binary search an implicit range `1..n` using an external comparison API
- [both] LeetCode 69 | Sqrt(x) | Easy | https://leetcode.com/problems/sqrtx/ | Binary search for the largest integer whose square does not exceed x
- [both] LeetCode 74 | Search a 2D Matrix | Medium | https://leetcode.com/problems/search-a-2d-matrix/ | Treat an m x n sorted matrix as a virtual 1D array of length `m * n` using `mid / cols` and `mid % cols`
- [both] LeetCode 33 | Search in Rotated Sorted Array | Medium | https://leetcode.com/problems/search-in-rotated-sorted-array/ | Identify which half of a rotated array is sorted on each step to maintain O(log n) search
- [B] LeetCode 875 | Koko Eating Bananas | Medium | https://leetcode.com/problems/koko-eating-bananas/ | Binary search on the answer space (eating speed) using a monotonic feasibility check
interview-questions:
- Why does `val mid = (low + high) / 2` risk integer overflow for large arrays, and how does `low + (high - low) / 2` or `(low + high) ushr 1` prevent it?
- How many comparisons at most does binary search need to search a sorted list of 1,000,000 items?
- Can you use binary search on a sorted `LinkedList` to get O(log n) time? Why or why not?
- What does Kotlin's `list.binarySearch(target)` return when the element is *not* found, and how do you recover the insertion index from that negative number?
- Why does binary search take at most 20 comparisons on 1,000,000 items and at most 30 comparisons on 1,000,000,000 items?
- Why should you write `val mid = low + (high - low) / 2` (or `(low + high) ushr 1`) instead of `(low + high) / 2`?
- What does Kotlin's `list.binarySearch(target)` return when the target is **not** present in the list, and how do you extract the insertion index from that return value?
- How do you recognize when an optimization problem can be solved by "binary searching on the answer"?

## dsa/searching/binary-search-boundaries
problems:
- [both] LeetCode 35 | Search Insert Position | Easy | https://leetcode.com/problems/search-insert-position/ | Find the lower bound (first index `>= target`) cleanly without off-by-one errors
- [both] LeetCode 278 | First Bad Version | Easy | https://leetcode.com/problems/first-bad-version/ | Binary search a boolean predicate `[false, false, true, true]` to find the exact boundary
- [both] LeetCode 34 | Find First and Last Position of Element in Sorted Array | Medium | https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/ | Run both lower-bound and upper-bound binary searches to handle duplicate targets
- [both] LeetCode 153 | Find Minimum in Rotated Sorted Array | Medium | https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/ | Converge `low < high` with `high = mid` vs `low = mid + 1` on a boundary condition
- [both] LeetCode 162 | Find Peak Element | Medium | https://leetcode.com/problems/find-peak-element/ | Use slope comparison `nums[mid] < nums[mid + 1]` to converge on a local peak in O(log n)
- [both] LeetCode 410 | Split Array Largest Sum | Hard | https://leetcode.com/problems/split-array-largest-sum/ | Binary search on the minimum possible maximum subarray sum using a greedy feasibility check
- [A] LeetCode 875 | Koko Eating Bananas | Medium | https://leetcode.com/problems/koko-eating-bananas/ | Binary search the answer space `[1, maxPile]` to find the minimum feasible rate
interview-questions:
- When does `low = mid` inside a `while (low < high)` loop cause an infinite loop when `high == low + 1`, and how does `mid = low + (high - low + 1) / 2` fix it?
- What is the difference between `lower_bound` (first element `>= target`) and `upper_bound` (first element `> target`)?
- How do you recognize a "Binary Search on Answer" problem where the input array itself is not sorted?
- What loop invariant holds true at the start and end of every iteration in a `while (low < high)` boundary binary search?
- Compare the loop condition `while (low <= high)` with `while (low < high)`. When do you use `high = mid - 1` versus `high = mid`?
- Why does writing `low = mid` inside `while (low < high)` cause an infinite loop when `high == low + 1`, and how does `mid = low + (high - low + 1) / 2` fix it?
- How do you find the count of a duplicate value `x` in a sorted array of 10 million elements in O(log n) time?
- What loop invariant holds for `low` and `high` throughout a lower-bound (`first index >= target`) binary search?

## dsa/trees/what-is-a-tree
problems:
- [both] LeetCode 2236 | Root Equals Sum of Children | Easy | https://leetcode.com/problems/root-equals-sum-of-children/ | Inspect a parent node and its immediate `left` and `right` child references
- [both] LeetCode 104 | Maximum Depth of Binary Tree | Easy | https://leetcode.com/problems/maximum-depth-of-binary-tree/ | Compute tree height recursively from leaf nodes (`null` = 0) up to the root
- [both] LeetCode 100 | Same Tree | Easy | https://leetcode.com/problems/same-tree/ | Verify structural and value equality of two hierarchical trees simultaneously
- [both] LeetCode 226 | Invert Binary Tree | Easy | https://leetcode.com/problems/invert-binary-tree/ | Recursively swap child subtrees at every internal node
- [both] LeetCode 543 | Diameter of Binary Tree | Easy | https://leetcode.com/problems/diameter-of-binary-tree/ | Distinguish the longest path *through* a node (`leftHeight + rightHeight`) from the height *returned* to its parent
- [A] LeetCode 101 | Symmetric Tree | Easy | https://leetcode.com/problems/symmetric-tree/ | Check mirror symmetry across left and right subtrees
- [B] LeetCode 236 | Lowest Common Ancestor of a Binary Tree | Medium | https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/ | Finds the deepest shared parent of two nodes in a hierarchy (just like finding the common parent `ViewGroup` of two Android `View`s)
interview-questions:
- Why does an Android View hierarchy or Compose UI tree form a tree rather than a general graph with cycles?
- What is the difference between the *depth* of a node and the *height* of a node in a tree?
- If a tree has `n` nodes, how many edges does it have, and how many paths exist from the root to any given node?
- What is the difference between a full binary tree, a complete binary tree, and a perfect binary tree?
- What makes a connected graph a **tree**, and why does a tree with n nodes always have exactly n - 1 edges?
- Define root, leaf, parent, ancestor, depth of a node, and height of a tree.
- How is an Android View hierarchy (or Jetpack Compose UI node tree) structured as an N-ary tree, and why can a View only have one parent?
- What is the maximum and minimum number of nodes in a binary tree of height h?

## dsa/trees/walking-a-tree
problems:
- [both] LeetCode 94 | Binary Tree Inorder Traversal | Easy | https://leetcode.com/problems/binary-tree-inorder-traversal/ | Visit `left -> root -> right` to process left subtrees before parent nodes
- [both] LeetCode 145 | Binary Tree Postorder Traversal | Easy | https://leetcode.com/problems/binary-tree-postorder-traversal/ | Visit `left -> right -> root` when children must be evaluated before their parent
- [both] LeetCode 105 | Construct Binary Tree from Preorder and Inorder Traversal | Medium | https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/ | Exploit how Preorder identifies the root while Inorder splits left and right subtrees
- [both] LeetCode 124 | Binary Tree Maximum Path Sum | Hard | https://leetcode.com/problems/binary-tree-maximum-path-sum/ | Combine postorder subtree gains with a global maximum tracker
- [A] LeetCode 144 | Binary Tree Preorder Traversal | Easy | https://leetcode.com/problems/binary-tree-preorder-traversal/ | Visit `root -> left -> right` both recursively and iteratively
- [A] LeetCode 112 | Path Sum | Easy | https://leetcode.com/problems/path-sum/ | Pass top-down state from root to leaf during a depth-first walk
- [A] LeetCode 236 | Lowest Common Ancestor of a Binary Tree | Medium | https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/ | Propagate found targets bottom-up in postorder to identify where two paths meet
- [B] HackerRank | Tree: Preorder Traversal | Easy | https://www.hackerrank.com/challenges/tree-preorder-traversal/problem | [Data Structures > Trees] Visits `Root -> Left -> Right` to process parents before children
- [B] LeetCode 297 | Serialize and Deserialize Binary Tree | Hard | https://leetcode.com/problems/serialize-and-deserialize-binary-tree/ | Encodes and reconstructs any binary tree using Preorder traversal with null markers
interview-questions:
- When measuring and laying out an Android UI tree, which steps naturally act like a top-down Preorder walk and which act like a bottom-up Postorder walk?
- Why can't you uniquely reconstruct a general binary tree from Preorder and Postorder traversals alone without Inorder?
- When deleting or freeing every node in a tree, why does Postorder traversal make the most sense?
- How do you write an iterative Inorder traversal using an explicit stack?
- Give one practical use case each for Preorder, Inorder, and Postorder traversal.
- Why does Android's layout measurement pass (`onMeasure`) naturally resemble a postorder/bottom-up tree walk when children wrap content?
- Why can you NOT uniquely reconstruct a general binary tree from Preorder and Postorder alone (without Inorder or null markers)?
- How do you implement an iterative Inorder traversal using an explicit `ArrayDeque` stack in Kotlin?

## dsa/trees/level-order-walk
problems:
- [both] LeetCode 102 | Binary Tree Level Order Traversal | Medium | https://leetcode.com/problems/binary-tree-level-order-traversal/ | Use a FIFO queue and snapshot `queue.size` at the start of each loop to group nodes by level
- [both] LeetCode 199 | Binary Tree Right Side View | Medium | https://leetcode.com/problems/binary-tree-right-side-view/ | Capture the last node processed in each level of a queue-driven walk
- [both] LeetCode 103 | Binary Tree Zigzag Level Order Traversal | Medium | https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/ | Alternate left-to-right and right-to-left collection on each level using a deque
- [A] LeetCode 111 | Minimum Depth of Binary Tree | Easy | https://leetcode.com/problems/minimum-depth-of-binary-tree/ | See why level-order BFS finds the shallowest leaf faster than DFS on a lopsided tree
- [A] LeetCode 637 | Average of Levels in Binary Tree | Easy | https://leetcode.com/problems/average-of-levels-in-binary-tree/ | Aggregate per-level statistics during a breadth-first walk
- [A] LeetCode 297 | Serialize and Deserialize Binary Tree | Hard | https://leetcode.com/problems/serialize-and-deserialize-binary-tree/ | Encode and reconstruct a binary tree using level-order BFS with `null` markers
- [B] HackerRank | Tree: Level Order Traversal | Easy | https://www.hackerrank.com/challenges/tree-level-order-traversal/problem | [Data Structures > Trees] Classic single-queue breadth-first walk from top to bottom
- [B] LeetCode 116 | Populating Next Right Pointers in Each Node | Medium | https://leetcode.com/problems/populating-next-right-pointers-in-each-node/ | Connects level-order neighbours in O(1) extra space by walking already-linked parent pointers
- [B] LeetCode 662 | Maximum Width of Binary Tree | Medium | https://leetcode.com/problems/maximum-width-of-binary-tree/ | Assigns heap-style indices `2*i` and `2*i + 1` during level-order BFS to measure level width
interview-questions:
- In a single-queue level-order traversal, how do you know where level `k` ends and level `k + 1` begins without storing the depth on every node?
- When finding the minimum depth of a binary tree with 100,000 nodes where the left branch has depth 1 and the right branch has depth 99,999, why is BFS vastly faster than DFS?
- What is the worst-case space complexity of a level-order BFS queue on a balanced binary tree of `n` nodes vs a DFS recursion stack?
- Why is the widest level of a complete binary tree roughly `n / 2` nodes?
- Why does a level-order walk use a **Queue** (FIFO) instead of a **Stack** (LIFO)?
- How does capturing `val levelSize = queue.size` before the inner `for` loop let you know exactly when one level ends and the next begins?
- For a balanced binary tree of n nodes, what is the maximum number of nodes held in the queue at one time during level-order traversal?Compare that memory to DFS on a skewed tree vs a wide balanced tree.
- If you want to find the shallowest leaf in a huge tree (`Minimum Depth of Binary Tree`), why is level-order BFS often much faster than DFS?

## dsa/bst/bst-search-and-insert
problems:
- [both] LeetCode 700 | Search in a Binary Search Tree | Easy | https://leetcode.com/problems/search-in-a-binary-search-tree/ | Prune half the tree at every step using the BST ordering property
- [both] LeetCode 701 | Insert into a Binary Search Tree | Medium | https://leetcode.com/problems/insert-into-a-binary-search-tree/ | Walk down to the unique `null` leaf slot where a new key belongs and attach it
- [both] LeetCode 98 | Validate Binary Search Tree | Medium | https://leetcode.com/problems/validate-binary-search-tree/ | Pass `(min, max)` bounds down so every descendant—not just the immediate child—obeys the BST invariant
- [both] LeetCode 235 | Lowest Common Ancestor of a Binary Search Tree | Medium | https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/ | Find the split point where `p` and `q` diverge in O(h) time and O(1) space
- [both] LeetCode 450 | Delete Node in a BST | Medium | https://leetcode.com/problems/delete-node-in-a-bst/ | Handle the 3 deletion cases: leaf, one child, and two children (replacing with the inorder successor)
- [A] LeetCode 938 | Range Sum of BST | Easy | https://leetcode.com/problems/range-sum-of-bst/ | Skip entire left or right subtrees when `node.val < low` or `node.val > high`
- [A] LeetCode 230 | Kth Smallest Element in a BST | Medium | https://leetcode.com/problems/kth-smallest-element-in-a-bst/ | Use the fact that an Inorder traversal of a BST visits keys in strictly sorted order
- [B] HackerRank | Binary Search Tree : Insertion | Easy | https://www.hackerrank.com/challenges/binary-search-tree-insertion/problem | [Data Structures > Trees] Walks down to the correct null child link and attaches the new value
interview-questions:
- Why isn't checking `node.left.val < node.val < node.right.val` at every node enough to prove a binary tree is a valid BST?
- Which traversal order of a Binary Search Tree produces its elements in sorted ascending order, and why?
- How do you delete a BST node that has *two* non-null children while preserving the BST ordering property?
- When would you choose `TreeMap` / `TreeSet` (Red-Black BST) over `HashMap` / `HashSet` in Kotlin?
- Why is checking `node.left.val < node.val && node.right.val > node.val` at every node NOT enough to prove a binary tree is a valid BST?
- What does an Inorder traversal of a valid Binary Search Tree produce, and how does that help solve `Kth Smallest Element in a BST`?
- How do you delete a BST node that has **two** children without violating the BST invariant?
- When would you choose a `TreeMap` / `TreeSet` in Kotlin over a `HashMap` / `HashSet`?

## dsa/bst/bst-shape-and-balance
problems:
- [both] LeetCode 108 | Convert Sorted Array to Binary Search Tree | Easy | https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/ | Repeatedly pick the middle element as root so the resulting BST has minimum height O(log n)
- [both] LeetCode 110 | Balanced Binary Tree | Easy | https://leetcode.com/problems/balanced-binary-tree/ | Check in O(n) postorder time whether every node's left and right subtrees differ in height by at most 1
- [both] LeetCode 109 | Convert Sorted List to Binary Search Tree | Medium | https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/ | Build a height-balanced BST from a sorted linked list in O(n) or O(n log n)
- [both] LeetCode 1382 | Balance a Binary Search Tree | Medium | https://leetcode.com/problems/balance-a-binary-search-tree/ | Extract sorted keys via Inorder traversal and rebuild a balanced BST of height O(log n)
- [A] LeetCode 897 | Increasing Order Search Tree | Easy | https://leetcode.com/problems/increasing-order-search-tree/ | Deliberately flatten a BST into a right-skewed chain to see what a degenerate O(n) BST looks like
- [B] HackerRank | Self Balancing Tree | Medium | https://www.hackerrank.com/challenges/self-balancing-tree/problem | [Data Structures > Balanced Trees] Implements left and right AVL tree rotations to restore balance after insertion
interview-questions:
- What shape does a plain Binary Search Tree take if you insert `[1, 2, 3, 4, 5, 6, 7]` in sorted order, and what happens to search complexity?
- Why must we repeatedly pick the median when building a balanced BST from a sorted array?
- How do self-balancing trees like AVL trees and Red-Black trees maintain O(log n) height during arbitrary insertions and deletions?
- Why did Java 8 choose Red-Black trees as the fallback for overloaded `HashMap` buckets instead of plain BSTs?
- What shape does a plain BST take if you insert `[1, 2, 3, 4, 5, 6, 7]` in ascending order, and what happens to the time complexity of `search()`?
- How does a tree rotation (left rotation / right rotation) change the height of subtrees while preserving the exact Inorder sequence of keys?
- What kind of self-balancing BST does Java/Kotlin's `TreeMap` use under the hood, and what height guarantee does it maintain?
- Why does Java 8+'s `HashMap` convert a bucket's linked list into a Red-Black Tree when a single bucket exceeds 8 colliding nodes?

## dsa/heaps/priority-queue-idea
problems:
- [both] LeetCode 1046 | Last Stone Weight | Easy | https://leetcode.com/problems/last-stone-weight/ | Repeatedly extract the two heaviest elements and push their difference back into a max-heap
- [both] LeetCode 703 | Kth Largest Element in a Stream | Easy | https://leetcode.com/problems/kth-largest-element-in-a-stream/ | Maintain a min-heap of size k so the kth largest element is always at `peek()` in O(1)
- [both] LeetCode 347 | Top K Frequent Elements | Medium | https://leetcode.com/problems/top-k-frequent-elements/ | Use a size-k min-heap ordered by frequency to avoid sorting all n distinct keys
- [both] LeetCode 621 | Task Scheduler | Medium | https://leetcode.com/problems/task-scheduler/ | Always schedule the most frequent remaining task first using a max-heap and cooldown queue
- [both] LeetCode 295 | Find Median from Data Stream | Hard | https://leetcode.com/problems/find-median-from-data-stream/ | Balance a max-heap (lower half) and a min-heap (upper half) for O(log n) insert and O(1) median
- [A] LeetCode 23 | Merge k Sorted Lists | Hard | https://leetcode.com/problems/merge-k-sorted-lists/ | Keep the current head of each of the k lists in a min-heap to always pull the global minimum in O(log k)
- [B] HackerRank | Jesse and Cookies | Easy | https://www.hackerrank.com/challenges/jesse-and-cookies/problem | [Data Structures > Heap] Repeatedly combines the two least sweet cookies using a Min-Heap
interview-questions:
- Counterintuitively, why do you use a *min-heap* of size `k` instead of a max-heap to find the *kth largest* element in a stream?
- In Kotlin/Java, does `PriorityQueue<Int>()` default to a min-heap or a max-heap, and how do you invert it?
- Why is finding the Top K items with a size-k heap O(n log k) better than sorting the entire array in O(n log n) when k is small?
- How does the two-heap pattern (`maxHeap` for the smaller half, `minHeap` for the larger half) maintain the median of a live data stream?
- Why do you use a **Min-Heap** of size k (instead of a Max-Heap) to track the **k largest** elements in a stream?
- What are the time complexities of `peek()`, ` offer()` / `add()`, and `poll()` on a `PriorityQueue` of size n?
- By default, is Java/Kotlin's `PriorityQueue` a Min-Heap or a Max-Heap, and how do you create the opposite one?
- Why does iterating over a `PriorityQueue` with a `for` loop NOT yield elements in sorted order?

## dsa/heaps/how-a-heap-works
problems:
- [both] HackerRank (Data Structures > Heap) | QHEAP1 | Easy | https://www.hackerrank.com/challenges/qheap1/problem | Practice core heap operations: insert, delete arbitrary element, and print minimum
- [both] LeetCode 912 | Sort an Array | Medium | https://leetcode.com/problems/sort-an-array/ | Implement in-place Heap Sort using `siftDown` (`heapify`) in O(n log n) time and O(1) auxiliary space
- [both] LeetCode 215 | Kth Largest Element in an Array | Medium | https://leetcode.com/problems/kth-largest-element-in-an-array/ | Build a heap in O(n) using bottom-up `heapify` and extract k times
- [A] HackerRank (Data Structures > Heap) | Jesse and Cookies | Easy | https://www.hackerrank.com/challenges/jesse-and-cookies/problem | Repeatedly pop the two smallest elements and push their combined value until threshold is met
- [A] LeetCode 373 | Find K Pairs with Smallest Sums | Medium | https://leetcode.com/problems/find-k-pairs-with-smallest-sums/ | Frontier-expand candidate index pairs in a min-heap without generating all n * m pairs
- [B] LeetCode 378 | Kth Smallest Element in a Sorted Matrix | Medium | https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/ | Pushes and sifts `(value, row, col)` tuples in a min-heap of size at most n
- [B] HackerRank | Find the Running Median | Hard | https://www.hackerrank.com/challenges/find-the-running-median/problem | [Data Structures > Heap] Maintains two array-backed heaps with rebalancing after every stream element
interview-questions:
- Why can a binary heap be stored in a plain flat array without any `left` or `right` node pointers?
- For a 0-indexed array heap, what are the exact index formulas for `parent(i)`, `leftChild(i)`, and `rightChild(i)`?
- Why does building a heap from an existing array of `n` elements bottom-up (`siftDown` from `n/2 - 1` down to `0`) take O(n) time instead of O(n log n)?
- Why doesn't a binary heap support O(log n) search for an arbitrary value the way a Binary Search Tree does?
- If a binary heap is stored in a 0-indexed array, what are the exact array indices of the parent, left child, and right child of node `i`?
- Why can a binary heap be stored in a flat array with zero wasted slots and no node objects, whereas a general binary search tree cannot?
- Walk through `siftUp` (bubble up) on `insert()` and `siftDown` (bubble down) on `poll()`. Why do we swap the root with the last element before sifting down on `poll()`?
- Why does building a heap bottom-up from an unsorted array (`heapify`) take O(n) time instead of O(n log n)?

## dsa/graphs/what-is-a-graph
problems:
- [both] LeetCode 1791 | Find Center of Star Graph | Easy | https://leetcode.com/problems/find-center-of-star-graph/ | Reason about node degrees where the center vertex has degree `n - 1`
- [both] LeetCode 997 | Find the Town Judge | Easy | https://leetcode.com/problems/find-the-town-judge/ | Distinguish `in-degree` (`n - 1`) from `out-degree` (`0`) in a directed graph
- [both] LeetCode 1971 | Find if Path Exists in Graph | Easy | https://leetcode.com/problems/find-if-path-exists-in-graph/ | Check connectivity between two vertices in an undirected graph
- [both] LeetCode 785 | Is Graph Bipartite? | Medium | https://leetcode.com/problems/is-graph-bipartite/ | 2-color a graph to check whether it contains any odd-length cycles
- [A] LeetCode 841 | Keys and Rooms | Medium | https://leetcode.com/problems/keys-and-rooms/ | Model rooms and keys as a directed graph and test whether all vertices are reachable from vertex 0
- [B] LeetCode 1557 | Minimum Number of Vertices to Reach All Nodes | Medium | https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/ | Identifies source nodes with in-degree 0 in a Directed Acyclic Graph (DAG)
interview-questions:
- What is the difference between a directed graph and an undirected graph, and how is a tree just a special case of a graph?
- In a social network like Instagram (following) vs Facebook (friends) vs Google Maps (road travel times), which are directed, undirected, and weighted?
- What do `in-degree` and `out-degree` mean in a directed graph?
- What is a Connected Component in an undirected graph, and what is a DAG (Directed Acyclic Graph)?
- What is the difference between a directed and an undirected graph, a weighted and an unweighted graph, and a cyclic and an acyclic graph?
- How is a Tree a special case of a Graph, and how is a Linked List a special case of a Tree?
- Give three examples of real graphs in an Android project (for example: Gradle module dependencies, Dagger/Hilt dependency graph, Navigation graph).
- In a directed graph, what do **in-degree** and **out-degree** mean, and what does the sum of all in-degrees equal?

## dsa/graphs/storing-a-graph
problems:
- [both] LeetCode 997 | Find the Town Judge | Easy | https://leetcode.com/problems/find-the-town-judge/ | Convert an edge list `Array<IntArray>` into degree arrays
- [both] LeetCode 133 | Clone Graph | Medium | https://leetcode.com/problems/clone-graph/ | Traverse and deep-copy an object-pointer adjacency representation using a visited map
- [both] LeetCode 547 | Number of Provinces | Medium | https://leetcode.com/problems/number-of-provinces/ | Traverse a graph given directly as an `n x n` adjacency matrix `isConnected[i][j]`
- [A] LeetCode 1971 | Find if Path Exists in Graph | Easy | https://leetcode.com/problems/find-if-path-exists-in-graph/ | Build an adjacency list `Array(n) { mutableListOf<Int>() }` from an undirected edge list
- [A] LeetCode 1557 | Minimum Number of Vertices to Reach All Nodes | Medium | https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/ | Represent a DAG compactly to find all vertices with in-degree 0
- [B] LeetCode 841 | Keys and Rooms | Medium | https://leetcode.com/problems/keys-and-rooms/ | Traverses a graph given directly as an **adjacency list** (`List<List<Int>>`)
- [B] LeetCode 200 | Number of Islands | Medium | https://leetcode.com/problems/number-of-islands/ | Treats a 2D character grid as an **implicit graph** where each cell's neighbours are `(r±1, c)` and `(r, c±1)`
interview-questions:
- What are the space and time trade-offs between an Adjacency Matrix (`O(V^2)`) and an Adjacency List (`O(V + E)`)?
- Why is an Adjacency List almost always preferred over an Adjacency Matrix for sparse real-world graphs like road networks?
- When given an undirected edge `[u, v]`, why must you add `v` to `adj[u]` *and* `u` to `adj[v]` when building an adjacency list?
- In grid problems like `Number of Islands`, why don't you need to build an explicit adjacency list when the 2D array already acts as an implicit graph?
- Compare an **Adjacency Matrix** (`Array<BooleanArray>`) and an **Adjacency List** (`Array<MutableList<Int>>`) in space complexity, checking if edge `(u, v)` exists, and iterating over all neighbours of `u`.
- What is a **sparse graph** vs a **dense graph**, and why would an adjacency matrix for a road network of 1,000,000 intersections blow up memory?
- When given a 2D grid problem (like `Number of Islands`), why shouldn't you build an explicit adjacency list first?
- How do you store edge weights in an adjacency list in Kotlin without boxing overhead?

## dsa/graphs/breadth-first-search
problems:
- [both] LeetCode 733 | Flood Fill | Easy | https://leetcode.com/problems/flood-fill/ | Run a basic queue-based BFS over 4-directional grid neighbors
- [both] LeetCode 994 | Rotting Oranges | Medium | https://leetcode.com/problems/rotting-oranges/ | Run multi-source BFS starting with all initial rotten oranges in the queue at level 0
- [both] LeetCode 1091 | Shortest Path in Binary Matrix | Medium | https://leetcode.com/problems/shortest-path-in-binary-matrix/ | Find the fewest 8-directional steps from top-left to bottom-right in an unweighted grid
- [both] LeetCode 752 | Open the Lock | Medium | https://leetcode.com/problems/open-the-lock/ | Model 4-digit lock states as an implicit graph and use BFS to find minimum wheel turns
- [both] LeetCode 127 | Word Ladder | Hard | https://leetcode.com/problems/word-ladder/ | Find the shortest word-transformation sequence in an unweighted word graph using BFS
- [B] HackerRank | Breadth First Search: Shortest Reach | Medium | https://www.hackerrank.com/challenges/bfsshortreach/problem | [Algorithms > Graph Theory] Computes shortest hop distances from a start node in an unweighted graph
interview-questions:
- Why does Breadth-First Search guarantee the shortest path (fewest edges) in an unweighted graph while Depth-First Search does not?
- In BFS, why must you mark a node as `visited` at the exact moment you *enqueue* it rather than waiting until you *dequeue* it?
- How does multi-source BFS (like `Rotting Oranges`) find the minimum distance from *any* starting node in a single O(V + E) pass?
- When does BFS fail to find the shortest path, requiring Dijkstra's algorithm instead?
- Why must you mark a node as `visited` **at the moment you enqueue it** rather than when you dequeue it? What bug happens on a grid if you mark it on dequeue?
- How does **Multi-Source BFS** work (like in `Rotting Oranges` or `01 Matrix`), and why does seeding the queue with all sources at step 0 take O(V + E) time?
- What is **Bidirectional BFS**, and why does searching simultaneously from `start` and `target` drastically cut the search space in `Word Ladder`?

## dsa/graphs/depth-first-search
problems:
- [both] LeetCode 200 | Number of Islands | Medium | https://leetcode.com/problems/number-of-islands/ | Count connected components in a 2D grid by sinking each island with DFS
- [both] LeetCode 695 | Max Area of Island | Medium | https://leetcode.com/problems/max-area-of-island/ | Accumulate and return the size of each connected component via recursive DFS
- [both] LeetCode 797 | All Paths From Source to Target | Medium | https://leetcode.com/problems/all-paths-from-source-to-target/ | Explore every path from node `0` to `n - 1` in a DAG using DFS with path backtracking
- [both] LeetCode 130 | Surrounded Regions | Medium | https://leetcode.com/problems/surrounded-regions/ | Run boundary-seeded DFS from the edges inward to mark regions that cannot be captured
- [both] LeetCode 329 | Longest Increasing Path in a Matrix | Hard | https://leetcode.com/problems/longest-increasing-path-in-a-matrix/ | Combine DFS with memoization on a directed acyclic grid graph
- [A] LeetCode 733 | Flood Fill | Easy | https://leetcode.com/problems/flood-fill/ | Explore connected regions recursively until hitting boundaries or different colors
- [B] HackerRank | Roads and Libraries | Medium | https://www.hackerrank.com/challenges/torque-and-development/problem | [Algorithms > Graph Theory] Counts connected component sizes with DFS to minimize infrastructure cost
interview-questions:
- Why must graph DFS maintain a `visited` set whereas tree DFS does not?
- How do you detect a cycle in an *undirected* graph vs a *directed* graph using DFS?
- In directed graph DFS, why do you need three states (`UNVISITED`, `VISITING` on current path, `VISITED` completely done) to detect a cycle?
- What is the time and space complexity of DFS on a graph with `V` vertices and `E` edges stored as an adjacency list?
- What is the time and space complexity of Depth-First Search on a graph with V vertices and E edges stored as an adjacency list?
- Why do we need 3 visitation states (`UNVISITED = 0`, `VISITING = 1` on current stack, `VISITED = 2` completed) to detect a cycle in a **directed** graph, whereas 2 states (`visited` boolean) suffice in an **undirected** graph (ignoring the immediate parent)?
- On a 1000 x 1000 grid in Android/JVM, why might a recursive DFS throw `StackOverflowError` while BFS or iterative DFS succeeds?
- When enumerating all paths (`All Paths From Source to Target`), why do you remove a node from the current path upon returning from the recursive DFS call, whereas in `Number of Islands` you leave cells permanently marked visited?

## dsa/graphs/dependency-order
problems:
- [both] LeetCode 207 | Course Schedule | Medium | https://leetcode.com/problems/course-schedule/ | Detect whether prerequisite dependencies contain a circular deadlock using Kahn's BFS or 3-color DFS
- [both] LeetCode 210 | Course Schedule II | Medium | https://leetcode.com/problems/course-schedule-ii/ | Output a valid topological execution order for tasks with prerequisites
- [both] LeetCode 802 | Find Eventual Safe States | Medium | https://leetcode.com/problems/find-eventual-safe-states/ | Use reverse-edge topological sorting (Kahn's algorithm) or cycle detection to find safe nodes
- [both] LeetCode 2115 | Find All Possible Recipes from Given Supplies | Medium | https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/ | Model recipes and ingredients as a dependency DAG and resolve buildable items via in-degrees
- [A] LeetCode 329 | Longest Increasing Path in a Matrix | Hard | https://leetcode.com/problems/longest-increasing-path-in-a-matrix/ | Find the longest dependency chain in an implicit DAG
- [B] LeetCode 310 | Minimum Height Trees | Medium | https://leetcode.com/problems/minimum-height-trees/ | Repeatedly peels degree-1 leaf layers inward (topological leaf trimming) to find the graph centroids
- [B] LeetCode 269 | Alien Dictionary | Hard | https://leetcode.com/problems/alien-dictionary/ | Extracts character ordering constraints from adjacent words and runs topological sort to deduce the alphabet
interview-questions:
- How does Gradle determine which Android modules can compile first and which modules must wait, and what error occurs if Module A depends on Module B and B depends on A?
- Walk through Kahn's algorithm for topological sorting using `in-degree` counts and a queue.
- Why does topological sort only exist for Directed Acyclic Graphs (DAGs)?
- How can you tell at the end of Kahn's algorithm whether the dependency graph contained a cycle?
- Walk through **Kahn's Algorithm** (in-degree + queue) for topological sort. How does it detect that the graph contains a cycle?
- How does Gradle determine which Android modules can be compiled in parallel and when a circular module dependency error must be thrown?
- In DFS-based topological sort, why do you push a node onto the result list in **postorder** (after visiting all its dependents) and then reverse the list?

## dsa/tries/what-is-a-trie
problems:
- [both] LeetCode 14 | Longest Common Prefix | Easy | https://leetcode.com/problems/longest-common-prefix/ | Understand shared string prefixes before generalizing to a branching prefix tree
- [both] LeetCode 208 | Implement Trie (Prefix Tree) | Medium | https://leetcode.com/problems/implement-trie-prefix-tree/ | Build `TrieNode` with `children` array and `isEndOfWord` flag to support `insert`, `search`, and `startsWith`
- [both] LeetCode 211 | Design Add and Search Words Data Structure | Medium | https://leetcode.com/problems/design-add-and-search-words-data-structure/ | Combine Trie lookup with DFS branching when encountering a `.` wildcard character
- [both] LeetCode 1268 | Search Suggestions System | Medium | https://leetcode.com/problems/search-suggestions-system/ | Return the top 3 lexicographically smallest autocomplete matches after each typed character
- [both] LeetCode 212 | Word Search II | Hard | https://leetcode.com/problems/word-search-ii/ | Prune grid backtracking simultaneously across thousands of dictionary words using a Trie
- [A] LeetCode 648 | Replace Words | Medium | https://leetcode.com/problems/replace-words/ | Store dictionary roots in a Trie to find the shortest matching prefix of each word in O(L) time
- [B] HackerRank | Contacts | Medium | https://www.hackerrank.com/challenges/contacts/problem | [Data Structures > Trie] Stores a subtree word count at each TrieNode to answer prefix-count queries in O(L) time
interview-questions:
- Why is a Trie better than a `HashSet<String>` for implementing an autocomplete search bar?
- What is the time complexity of `startsWith(prefix)` in a Trie of 1,000,000 words when the prefix has length `L = 5`?
- Why is the `isEndOfWord` boolean needed on a `TrieNode` if `"app"` and `"apple"` are both inserted?
- When should a `TrieNode` store children in `Array<TrieNode?>(26)` vs `HashMap<Char, TrieNode>`?
- Why does a Trie find whether any word starts with a prefix of length L in O(L) time independent of whether the dictionary has 100 or 10,000,000 words?
- Why is the `isEndOfWord` boolean flag necessary on a `TrieNode`? What goes wrong if you insert `"app"` and `"apple"` without it?
- Compare storing children in a `TrieNode` as `Array<TrieNode?>(26)` versus `HashMap<Char, TrieNode>`: when is each better?
- Why is a `HashSet<String>` usually better for exact word lookup (`contains("apple")`), whereas a `Trie` is vastly better for autocomplete (`startsWith("ap")`) and spell-checking?

## dsa/patterns/two-pointers
problems:
- [both] LeetCode 125 | Valid Palindrome | Easy | https://leetcode.com/problems/valid-palindrome/ | Walk `left` and `right` pointers inward while skipping non-alphanumeric characters in O(n) time and O(1) space
- [both] LeetCode 283 | Move Zeroes | Easy | https://leetcode.com/problems/move-zeroes/ | Use same-direction slow (`write`) and fast (`read`) pointers to partition in-place
- [both] LeetCode 167 | Two Sum II - Input Array Is Sorted | Medium | https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/ | Exploit sorted order to move `left++` when sum is too small and `right--` when sum is too large
- [both] LeetCode 11 | Container With Most Water | Medium | https://leetcode.com/problems/container-with-most-water/ | Greedily advance the shorter boundary pointer inward to maximize area in O(n)
- [both] LeetCode 15 | 3Sum | Medium | https://leetcode.com/problems/3sum/ | Fix one element and run a converging two-pointer sweep with duplicate skipping in O(n^2)
- [both] LeetCode 42 | Trapping Rain Water | Hard | https://leetcode.com/problems/trapping-rain-water/ | Maintain `leftMax` and `rightMax` across two converging pointers to compute trapped water in O(1) space
interview-questions:
- In `Two Sum II` on a sorted array, why is it safe to discard the entire rest of the row/column when moving `left++` or `right--`?
- What are the two main flavors of the Two Pointers pattern (opposite-end converging pointers vs same-direction slow/fast pointers)?
- In `Container With Most Water`, why must you always move the pointer pointing to the *shorter* line rather than the taller line?
- Why does the two-pointer approach for `Two Sum` require the array to be sorted first, whereas the `HashMap` approach works on unsorted arrays?
- In `Two Sum II` (sorted array), prove why moving `right--` when `nums[left] + nums[right] > target` can never accidentally skip the valid pair.
- What are the two main flavors of the Two Pointers pattern (converging opposite-ends vs same-direction fast/slow reader/writer), and what problems does each solve?
- In `Container With Most Water`, why is it always safe to move the pointer at the **shorter** line inward?
- When does a Two Pointers approach require sorting the array first, and why wouldn't Two Pointers + Sort work directly on the original `1. Two Sum` if it asks for original indices (unless you pair values with their original indices)?

## dsa/patterns/sliding-window
problems:
- [both] LeetCode 643 | Maximum Average Subarray I | Easy | https://leetcode.com/problems/maximum-average-subarray-i/ | Slide a fixed-size window of length k by adding `nums[i]` and subtracting `nums[i - k]`
- [both] LeetCode 3 | Longest Substring Without Repeating Characters | Medium | https://leetcode.com/problems/longest-substring-without-repeating-characters/ | Expand `right` and advance `left` past duplicates to find the longest valid variable-size window
- [both] LeetCode 209 | Minimum Size Subarray Sum | Medium | https://leetcode.com/problems/minimum-size-subarray-sum/ | Expand `right` until sum >= target, then shrink `left` as much as possible to minimize window length
- [both] LeetCode 424 | Longest Repeating Character Replacement | Medium | https://leetcode.com/problems/longest-repeating-character-replacement/ | Maintain `windowLength - maxFreq <= k` over a sliding character frequency window
- [both] LeetCode 76 | Minimum Window Substring | Hard | https://leetcode.com/problems/minimum-window-substring/ | Master the expand-until-valid / shrink-to-optimize template on character frequency maps
- [A] LeetCode 121 | Best Time to Buy and Sell Stock | Easy | https://leetcode.com/problems/best-time-to-buy-and-sell-stock/ | Track the lowest buy price as the left anchor of an expanding window
- [B] LeetCode 567 | Permutation in String | Medium | https://leetcode.com/problems/permutation-in-string/ | Fixed-length sliding character frequency window matching a target anagram
interview-questions:
- What is the difference between a fixed-size sliding window and a dynamic (variable-size) sliding window?
- Why does `Minimum Size Subarray Sum` sliding window work in O(n) only when all array numbers are positive, and what breaks if the array contains negative numbers?
- Why is the dynamic sliding window (`for right` with an inner `while` shrinking `left`) O(n) total time instead of O(n^2)?
- What state do you typically maintain inside a sliding window so you don't recompute the window's validity from scratch on every step?
- What is the difference between a **fixed-size** sliding window and a **variable-size** sliding window?
- Why does the variable-size sliding window for `Minimum Size Subarray Sum` fail if the array can contain **negative** numbers?
- Explain clearly why a variable sliding window with `for (right in nums.indices) { while (...) { left++ } }` executes at most `2n` pointer movements total, not `n^2`.
- In `Longest Substring Without Repeating Characters`, how can storing each character's last seen index in a map let `left` jump directly past the duplicate instead of stepping one by one?

## dsa/patterns/prefix-sums
problems:
- [both] LeetCode 1480 | Running Sum of 1d Array | Easy | https://leetcode.com/problems/running-sum-of-1d-array/ | Build the foundational cumulative sum array `prefix[i] = prefix[i - 1] + nums[i]`
- [both] LeetCode 303 | Range Sum Query - Immutable | Easy | https://leetcode.com/problems/range-sum-query-immutable/ | Answer arbitrary `sumRange(left, right)` queries in O(1) time using `prefix[right + 1] - prefix[left]`
- [both] LeetCode 724 | Find Pivot Index | Easy | https://leetcode.com/problems/find-pivot-index/ | Compare left prefix sum against `totalSum - leftSum - nums[i]` in O(1) space
- [both] LeetCode 560 | Subarray Sum Equals K | Medium | https://leetcode.com/problems/subarray-sum-equals-k/ | Combine running prefix sums with a `HashMap` to count subarrays summing to k (including negatives) in O(n)
- [both] LeetCode 304 | Range Sum Query 2D - Immutable | Medium | https://leetcode.com/problems/range-sum-query-2d-immutable/ | Generalize prefix sums to 2D rectangles using inclusion-exclusion in O(1) per query
- [A] LeetCode 523 | Continuous Subarray Sum | Medium | https://leetcode.com/problems/continuous-subarray-sum/ | Store `prefixSum % k` remainders in a map to spot subarray multiples of k
- [B] HackerRank | Array Manipulation | Hard | https://www.hackerrank.com/challenges/crush/problem | [Data Structures > Arrays] Uses the inverse of prefix sums (a difference array: `+k` at `a`, `-k` at `b+1`) to apply m range updates in O(n + m)
interview-questions:
- Why do we usually allocate a prefix sum array of size `n + 1` with `prefix[0] = 0` instead of size `n`?
- How does `Subarray Sum Equals K` combine prefix sums and a `HashMap` to handle arrays with negative numbers where a sliding window fails?
- In a 2D prefix sum grid, why does computing a sub-rectangle sum require adding back the top-left corner `prefix[r1][c1]` (inclusion-exclusion)?
- When the underlying array is updated frequently (`update(index, val)`), why do plain prefix sums become slow (O(n) per update) and what structure replaces them?
- Why do we usually allocate a prefix sum array of size `n + 1` with `prefix[0] = 0` so that `sum(left..right) = prefix[right + 1] - prefix[left]`?
- Why does `Subarray Sum Equals K` (LeetCode 560) use Prefix Sums + HashMap instead of a Sliding Window?
- How does a **Difference Array** allow you to add `v` to every element in `arr[L..R]` across 100,000 queries in O(1) per query before taking a final prefix sum?
- In 2D prefix sums, write the inclusion-exclusion formula for the sum of rectangle `(r1, c1)` to `(r2, c2)` and explain why the top-left corner is added back.

## dsa/patterns/choose-the-right-structure
problems:
- [both] LeetCode 146 | LRU Cache | Medium | https://leetcode.com/problems/lru-cache/ | Combine a `HashMap` (for O(1) key lookup) with a doubly linked list (for O(1) recency reordering)
- [both] LeetCode 380 | Insert Delete GetRandom O(1) | Medium | https://leetcode.com/problems/insert-delete-getrandom-o1/ | Combine an `ArrayList` (for O(1) random index access) with a `HashMap` (for O(1) value-to-index lookup)
- [both] LeetCode 981 | Time Based Key-Value Store | Medium | https://leetcode.com/problems/time-based-key-value-store/ | Combine a `HashMap<String, MutableList<Pair<Int, String>>>` with binary search on monotonic timestamps
- [both] LeetCode 295 | Find Median from Data Stream | Hard | https://leetcode.com/problems/find-median-from-data-stream/ | Choose two opposite-ordered heaps over a sorted list to get O(log n) insertion and O(1) median
- [A] LeetCode 359 | Logger Rate Limiter | Easy | https://leetcode.com/problems/logger-rate-limiter/ | Choose a `HashMap<String, Int>` (or queue + set) to deduplicate messages within a 10-second window
- [A] LeetCode 460 | LFU Cache | Hard | https://leetcode.com/problems/lfu-cache/ | Combine a key map, a frequency-to-doubly-linked-list map, and `minFreq` tracking for all-O(1) operations
- [B] LeetCode 355 | Design Twitter | Medium | https://leetcode.com/problems/design-twitter/ | Combines a `HashMap` of follower `HashSet`s, per-user tweet lists, and a `PriorityQueue` k-way merge for the news feed
- [B] LeetCode 432 | All O`one Data Structure | Hard | https://leetcode.com/problems/all-oone-data-structure/ | Pairs a `HashMap` with a doubly linked list of count-buckets to maintain `inc`, `dec`, `getMaxKey`, and `getMinKey` all in O(1)
interview-questions:
- When a single data structure cannot satisfy all required O(1) or O(log n) operations, how do you combine two structures (like `HashMap` + `DoublyLinkedList` or `HashMap` + `ArrayList`)?
- Which data structure would you choose for: (a) undo/redo in a text editor, (b) autocomplete prefix matching, (c) finding the highest-priority background job, and (d) checking if a user ID is blocked?
- In `Time Based Key-Value Store`, why can you use a plain `ArrayList` + binary search instead of a `TreeMap` if timestamps are guaranteed to be strictly increasing?
- How do you translate problem constraints (`n <= 20`, `n <= 10^4`, `n <= 10^5`) into the target time complexity and candidate data structures before writing code?
- If an interviewer asks you to design a data structure supporting operations X, Y, and Z in specific time complexities, how do you systematically combine two structures (such as `HashMap` + `ArrayList` or `HashMap` + `LinkedList`) to cover each other's weaknesses?
- Which data structure would you choose for an Android in-memory image cache that must evict the least recently used bitmap when total byte size exceeds 32 MB?
- You need to store sensor readings arriving in timestamp order and frequently query "what was the latest reading at or before time T for sensor ID S?". Which combination of data structures gives the best time and memory complexity?
- Compare `HashMap`, `TreeMap`, `LinkedHashMap`, and `ArrayDeque`: what is the unique superpower of each one?
