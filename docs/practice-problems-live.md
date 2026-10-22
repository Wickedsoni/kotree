# Practice problems on live pages (generated, read-only)


## daa\analysis\best-worst-average-case

1. LeetCode 28, Find the Index of the First Occurrence in a String. A plain search through the text is quick on most inputs and slow on texts like "aaaa...ab". https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/
2. HackerRank, Running Time of Algorithms (Algorithms, Sorting). Count the shifts insertion sort does on a sorted list (the best case) and on a reversed list (the worst case). https://www.hackerrank.com/challenges/runningtime/problem
3. LeetCode 81, Search in Rotated Sorted Array II. Count the checks when the list has repeated values and you cannot tell which half is sorted. https://leetcode.com/problems/search-in-rotated-sorted-array-ii/
4. LeetCode 912, Sort an Array. Count the steps a sort takes on a sorted list and on a reversed list. https://leetcode.com/problems/sort-an-array/
5. LeetCode 215, Kth Largest Element in an Array. Compare a method that sorts the whole list with one that stops once it finds the answer. https://leetcode.com/problems/kth-largest-element-in-an-array/

## daa\analysis\big-o-omega-theta

1. LeetCode 704, Binary Search (Easy). The best case is 1 check and the worst case is about log n checks. Say which case your bound covers. https://leetcode.com/problems/binary-search/
2. LeetCode 268, Missing Number (Easy). Every number must be looked at, so no method can use fewer than about n steps. Show a method that uses about n. https://leetcode.com/problems/missing-number/
3. LeetCode 75, Sort Colors (Medium). A one-pass method takes a number of steps tight to n and uses a fixed amount of extra memory. https://leetcode.com/problems/sort-colors/
4. LeetCode 148, Sort List (Medium). Merge sort takes about n log n steps whatever the input, so the bound is tight. https://leetcode.com/problems/sort-list/
5. LeetCode 41, First Missing Positive (Hard). The statement asks for time tight to n with fixed extra memory, which a swap-into-place method achieves. https://leetcode.com/problems/first-missing-positive/

## daa\analysis\loop-invariants

1. LeetCode 704, Binary Search. The range from low to high always holds the target if it is in the array. https://leetcode.com/problems/binary-search/
2. LeetCode 26, Remove Duplicates from Sorted Array. The front part of the array holds every different value seen so far, in order. https://leetcode.com/problems/remove-duplicates-from-sorted-array/
3. LeetCode 283, Move Zeroes. The front part holds the non-zero values seen so far, in their original order. https://leetcode.com/problems/move-zeroes/
4. LeetCode 53, Maximum Subarray. After each turn, the running value is the best sum of a stretch that ends at the current position. https://leetcode.com/problems/maximum-subarray/
5. LeetCode 75, Sort Colors. The array is split into three finished parts and one part not yet looked at. https://leetcode.com/problems/sort-colors/
6. LeetCode 42, Trapping Rain Water. Two pointers move inward, and the side with the lower wall decides the water at its position. https://leetcode.com/problems/trapping-rain-water/

## dsa\arrays\arrays-in-memory

1. LeetCode 66, Plus One (Easy). Walk the digits from the last index backwards and handle the carry. https://leetcode.com/problems/plus-one/
2. LeetCode 121, Best Time to Buy and Sell Stock (Easy). One pass over the array, reading each spot in order. https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
3. LeetCode 189, Rotate Array (Medium). Move items to new indexes in place, using three reversals. https://leetcode.com/problems/rotate-array/
4. LeetCode 54, Spiral Matrix (Medium). Keep every index inside the edges of a 2D array. https://leetcode.com/problems/spiral-matrix/
5. LeetCode 48, Rotate Image (Medium). Map each row and column to its new place without leaving the array. https://leetcode.com/problems/rotate-image/

## dsa\arrays\how-arraylist-grows

1. LeetCode 1929, Concatenation of Array (Easy). Build a new array twice the size and copy into it. https://leetcode.com/problems/concatenation-of-array/
2. LeetCode 118, Pascal's Triangle (Easy). Grow a list of rows, each with a size you know in advance. https://leetcode.com/problems/pascals-triangle/
3. HackerRank Dynamic Array (Easy), in Data Structures, Arrays. Append to lists that grow as queries arrive. https://www.hackerrank.com/challenges/dynamic-array/problem
4. LeetCode 443, String Compression (Medium). Compare appending to a growing buffer with packing the array in place. https://leetcode.com/problems/string-compression/
5. LeetCode 380, Insert Delete GetRandom O(1) (Medium). Pair a growing list with a map so add and delete stay fast. https://leetcode.com/problems/insert-delete-getrandom-o1/

## dsa\arrays\insert-and-delete-in-an-array

1. LeetCode 27, Remove Element (Easy). Overwrite the unwanted items in one pass instead of shifting after every removal. https://leetcode.com/problems/remove-element/
2. LeetCode 26, Remove Duplicates from Sorted Array (Easy). Pack the unique items to the front with a reading spot and a writing spot. https://leetcode.com/problems/remove-duplicates-from-sorted-array/
3. LeetCode 88, Merge Sorted Array (Easy). Write from the back so unread items are not overwritten. https://leetcode.com/problems/merge-sorted-array/
4. HackerRank Left Rotation (Easy), in Data Structures, Arrays. See why shifting left one by one is slow. https://www.hackerrank.com/challenges/array-left-rotation/problem
5. LeetCode 80, Remove Duplicates from Sorted Array II (Medium). Pack the items to the front when up to two copies are allowed. https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/

## dsa\complexity\big-o-notation

1. LeetCode 704, Binary Search (Easy). Halve the range each time and compare O(log n) with a plain O(n) scan. https://leetcode.com/problems/binary-search/
2. LeetCode 169, Majority Element (Easy). Compare sorting, a counting map and a one-pass voting method, and say the time and the extra memory of each. https://leetcode.com/problems/majority-element/
3. LeetCode 50, Pow(x, n) (Medium). Cut n multiplications down to about log n by squaring. https://leetcode.com/problems/powx-n/
4. LeetCode 4, Median of Two Sorted Arrays (Hard). The statement asks for O(log(m + n)) time, where merging the two lists is too slow. https://leetcode.com/problems/median-of-two-sorted-arrays/

## dsa\complexity\counting-steps

1. LeetCode 1480, Running Sum of 1d Array. Count the loop runs for one pass over n numbers. https://leetcode.com/problems/running-sum-of-1d-array/
2. LeetCode 1342, Number of Steps to Reduce a Number to Zero. Count the operations as you halve or subtract, and watch how the count grows with the input. https://leetcode.com/problems/number-of-steps-to-reduce-a-number-to-zero/
3. LeetCode 268, Missing Number. Compare a method with several passes against one pass over the list. https://leetcode.com/problems/missing-number/
4. LeetCode 1512, Number of Good Pairs. Count the steps of checking every pair with a loop inside a loop. https://leetcode.com/problems/number-of-good-pairs/

## dsa\complexity\reading-complexity-in-code

1. LeetCode 283, Move Zeroes (Easy). Spot how shifting items inside a loop hides O(n²) work, then fix it in O(n). https://leetcode.com/problems/move-zeroes/
2. LeetCode 14, Longest Common Prefix (Easy). Count two loops whose bounds depend on the number of strings and on their length. https://leetcode.com/problems/longest-common-prefix/
3. LeetCode 209, Minimum Size Subarray Sum (Medium). See why a `while` inside a `for` stays O(n) when both pointers only move forward. https://leetcode.com/problems/minimum-size-subarray-sum/
4. LeetCode 15, 3Sum (Medium). Combine a sort of O(n log n) with a scan to reach O(n²) instead of O(n³). https://leetcode.com/problems/3sum/
5. LeetCode 84, Largest Rectangle in Histogram (Hard). Show that each item is pushed and popped once, so the total is O(n). https://leetcode.com/problems/largest-rectangle-in-histogram/

## dsa\complexity\time-vs-space

1. LeetCode 217, Contains Duplicate (Easy). Spend a set of extra memory to cut the time from sorting to one pass. https://leetcode.com/problems/contains-duplicate/
2. LeetCode 1, Two Sum (Easy). Keep a map of numbers seen so far, instead of checking every pair. https://leetcode.com/problems/two-sum/
3. LeetCode 136, Single Number (Easy). A map works, then a bit trick gets the memory back to a constant. https://leetcode.com/problems/single-number/
4. LeetCode 238, Product of Array Except Self (Medium). Build the answer with little extra memory instead of two helper arrays. https://leetcode.com/problems/product-of-array-except-self/
5. LeetCode 41, First Missing Positive (Hard). Reuse the input array as the storage, so extra space stays constant. https://leetcode.com/problems/first-missing-positive/

## dsa\linked-lists\insert-and-remove-nodes

1. LeetCode 203, Remove Linked List Elements (Easy). Use a dummy head so that removing at the front looks the same as removing in the middle. https://leetcode.com/problems/remove-linked-list-elements/
2. LeetCode 237, Delete Node in a Linked List (Medium). Remove a node when you are not given the node in front. https://leetcode.com/problems/delete-node-in-a-linked-list/
3. LeetCode 19, Remove Nth Node From End of List (Medium). Keep a gap between two walkers to stop one dancer early. https://leetcode.com/problems/remove-nth-node-from-end-of-list/
4. LeetCode 25, Reverse Nodes in k-Group (Hard). Combine counting a group, reversing it and reconnecting the ends. https://leetcode.com/problems/reverse-nodes-in-k-group/

## dsa\linked-lists\linked-list-vs-arraylist

1. LeetCode 21, Merge Two Sorted Lists (Easy). Splice existing nodes together without copying them into a new row. https://leetcode.com/problems/merge-two-sorted-lists/
2. LeetCode 234, Palindrome Linked List (Easy). See the extra work needed when a chain has no read-by-position. https://leetcode.com/problems/palindrome-linked-list/
3. LeetCode 707, Design Linked List (Medium). Build `get`, add at the head, add at the tail, add at an index and delete at an index, and feel each cost. https://leetcode.com/problems/design-linked-list/
4. LeetCode 1472, Design Browser History (Medium). Compare a chain of pages against a row with a cursor for visit, back and forward. https://leetcode.com/problems/design-browser-history/
5. LeetCode 143, Reorder List (Medium). Find the middle, reverse half and merge, all without reading by position. https://leetcode.com/problems/reorder-list/
6. LeetCode 146, LRU Cache (Medium). The textbook case where a doubly linked list plus a map beats a row. https://leetcode.com/problems/lru-cache/
7. LeetCode 460, LFU Cache (Hard). Keep one doubly linked list per use count so eviction stays quick. https://leetcode.com/problems/lfu-cache/

## dsa\linked-lists\what-is-a-linked-list

1. LeetCode 876, Middle of the Linked List (Easy). Use a slow walker and a fast walker to find the middle in one pass. https://leetcode.com/problems/middle-of-the-linked-list/
2. LeetCode 141, Linked List Cycle (Easy). Detect whether the notes loop back on themselves, using no extra memory. https://leetcode.com/problems/linked-list-cycle/
3. LeetCode 142, Linked List Cycle II (Medium). Find the node where the loop starts. https://leetcode.com/problems/linked-list-cycle-ii/
4. LeetCode 138, Copy List with Random Pointer (Medium). Copy a chain whose nodes hold a second, arbitrary reference. https://leetcode.com/problems/copy-list-with-random-pointer/

## dsa\stacks\matching-brackets

1. LeetCode 20, Valid Parentheses (Easy). Push openers and check that every closer fits the top. https://leetcode.com/problems/valid-parentheses/
2. LeetCode 1614, Maximum Nesting Depth of the Parentheses (Easy). Track how many boxes are open at once to find the deepest nesting. https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/
3. LeetCode 921, Minimum Add to Make Parentheses Valid (Medium). Count the unmatched openers and closers to repair a line. https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/
4. HackerRank, Balanced Brackets (Medium). Handle a closer with nothing open and openers left at the end. https://www.hackerrank.com/challenges/balanced-brackets/problem
5. LeetCode 1249, Minimum Remove to Make Valid Parentheses (Medium). Push positions so you know exactly which brackets to delete. https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/
6. LeetCode 856, Score of Parentheses (Medium). Keep a running score for each open box on the stack. https://leetcode.com/problems/score-of-parentheses/
7. LeetCode 32, Longest Valid Parentheses (Hard). Use a stack of positions to measure the longest balanced stretch. https://leetcode.com/problems/longest-valid-parentheses/

## dsa\stacks\what-is-a-stack

1. LeetCode 682, Baseball Game (Easy). Push scores and pop or peek at earlier ones, like undo and history. https://leetcode.com/problems/baseball-game/
2. LeetCode 1047, Remove All Adjacent Duplicates In String (Easy). Use the top of the stack to cancel a matching neighbour. https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/
3. LeetCode 232, Implement Queue using Stacks (Easy). Pour items between two stacks to turn last-in-first-out into first-in-first-out. https://leetcode.com/problems/implement-queue-using-stacks/
4. LeetCode 225, Implement Stack using Queues (Easy). Build a stack out of a queue to see both orders side by side. https://leetcode.com/problems/implement-stack-using-queues/
5. LeetCode 155, Min Stack (Medium). Track the smallest item so push, pop, top and minimum are all fast. https://leetcode.com/problems/min-stack/
6. LeetCode 150, Evaluate Reverse Polish Notation (Medium). Evaluate sums written like `3 4 +`: push numbers, and pop two at each sign. https://leetcode.com/problems/evaluate-reverse-polish-notation/
7. LeetCode 739, Daily Temperatures (Medium). Keep days still waiting for a warmer one on a stack, and clear the colder ones when a warmer day arrives. https://leetcode.com/problems/daily-temperatures/
8. LeetCode 42, Trapping Rain Water (Hard). Use a stack of bar positions to fill the gaps between taller bars. https://leetcode.com/problems/trapping-rain-water/

## dsa\strings\compare-text-by-characters

1. LeetCode 242, Valid Anagram (Easy). Use a 26-slot counter to compare two words in one pass each. https://leetcode.com/problems/valid-anagram/
2. LeetCode 383, Ransom Note (Easy). Check that one text has enough of every tile to build another. https://leetcode.com/problems/ransom-note/
3. LeetCode 387, First Unique Character in a String (Easy). Count every letter first, then find the first letter whose count is 1. https://leetcode.com/problems/first-unique-character-in-a-string/
4. LeetCode 49, Group Anagrams (Medium). Group words by their letter counts without sorting each word. https://leetcode.com/problems/group-anagrams/
5. LeetCode 438, Find All Anagrams in a String (Medium). Keep a moving window of 26 counts and match it against a word. https://leetcode.com/problems/find-all-anagrams-in-a-string/

## dsa\strings\text-building-cost

1. LeetCode 1108, Defanging an IP Address (Easy). Build a changed copy of a string in one pass with a builder. https://leetcode.com/problems/defanging-an-ip-address/
2. LeetCode 151, Reverse Words in a String (Medium). Split the text, then join the words in reverse order without repeated `+=`. https://leetcode.com/problems/reverse-words-in-a-string/
3. LeetCode 394, Decode String (Medium). Build repeated pieces with a builder instead of joining again and again. https://leetcode.com/problems/decode-string/
4. LeetCode 68, Text Justification (Hard). Build each line of exact width in one pass. https://leetcode.com/problems/text-justification/
