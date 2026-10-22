# Review DSA-A (22 Oct 2026)

Leaves (all `status: shadow`): `dsa/complexity/time-vs-space`, `dsa/arrays/arrays-in-memory`, `dsa/arrays/insert-and-delete-in-an-array`, `dsa/arrays/how-arraylist-grows`.
Checked against `docs/lesson-checklist.md` (all 14 items), ADR 009, the roadmap rows in `content/dsa/_roadmap.md` and `docs/practice/practice-dsa.md`.

## Build

`./gradlew :site:validateContent --console=plain` printed `validateContent: ok`, BUILD SUCCESSFUL. No errors and no warnings (all four sim blocks pass SM0 to SM11; frontmatter `sims` equals fence ids; CH rules pass for shadow). The browser pass of `/preview/<id>` (mouse, keyboard, reduced motion, live region) was NOT done; sims were hand-traced against `SimCore.kt`, `TryProgress.kt` and `CompareLayout.kt` instead, as in review L3-a. It stays owed to the layer build.

## Kotlin run (`bash scripts/kt-run.sh`, kotlinc 2.4.21, no compiler warnings)

All 13 challenge blocks and all 6 Name it / Try it programs compile and print exactly the stated `output` / `expected`. Broken versions of the Fix blocks also behave as the prose says.

| Leaf | Block | Result |
|---|---|---|
| time-vs-space | Name it | `Trips to the station: 2`, `Pages in the pocket: 2` OK |
| | repeats-use-the-pocket | `2 2` OK (key c) |
| | all-new-questions | `4 4` OK (key a) |
| | fix-endless-cache | bug prints 1000 / 1000; fix prints 1000 / 100 OK |
| | Try it Break (1..1000) | 1000 / 1000 OK; Fix (braces, `clear` at 100) 1000 / 100 OK |
| arrays-in-memory | Name it | `40 85 5 4` OK |
| | write-then-read-spot | `6`, `0` OK (key b) |
| | size-and-last-index | `4 3 40` OK (key c) |
| | fix-loop-one-too-far | bug throws `ArrayIndexOutOfBoundsException`; fix prints `Readings: 5`, `Total: 112` OK |
| | Try it Break `scores[5]` | prints 40 then throws OK; Fix `lastIndex` prints `40 100 5 4` OK |
| insert-and-delete | Name it | `[A, B, C, D]`, `[B, C, D]`, `[Z, B, C, D]` OK |
| | Try it program | `Front: 499500`, `Back: 0` OK; Break 2000 gives `1999000` / `0` OK |
| | remove-first-of-four | `[Ben, Cy, Di]`, `Ben` OK (key b) |
| | insert-in-the-middle | `[Ana, Zed, Ben, Cy]`, `[Ana, Zed, Ben]` OK (key c) |
| | count-the-moves | `4 5` OK (key a) |
| | fix-remove-first-in-loop | bug `Done: 1000`, `Shifted: 499500`; fix `Shifted: 0` OK |
| how-arraylist-grows | Name it | `Size: 8`, `Items copied in total: 6` OK |
| | copies-for-eight-adds | `7 8` OK (key b) |
| | copies-when-growing-by-one | `10` OK (key a) |
| | fix-plus-in-a-loop | bug `New lists built: 1000`; fix `0`, `Rows: 1000` OK |
| | Try it Break `+ 1` | copies 27 OK; Fix `IntArray(8)` copies 0 OK |

Correct letters vary inside each leaf. Every wrong option is plausible and its `why` leads to it.

## Facts checked against official pages

- Kotlin arrays page (kotlinlang.org/docs/arrays.html): `ArrayIndexOutOfBoundsException` at runtime, primitive arrays "store values without boxing" vs "Usually boxed", `IntArray` maps to `int[]`, `size` / `lastIndex`, "Size: Fixed", `copyOf`. All match.
- Kotlin `getOrPut`, `MutableList.removeAt` ("All elements placed after index are shifted 1 position left"), `ArrayList` typealias to `java.util.ArrayList` on JVM, `ArrayList(initialCapacity)`, `ensureCapacity` "Does nothing" on JS, `+=` on `var` read-only vs `val` mutable collection. All match.
- Java SE 21 `ArrayList` (capacity, growth policy "not specified", amortized constant time, "adding n elements requires O(n) time", `ensureCapacity`, `trimToSize`, shifting in `add(int,E)` and `remove(int)`). All match. The Android reference page `developer.android.com/reference/java/util/ArrayList` carries the same wording (confirmed by search; the page itself is too large for the fetch tool).
- Android memory page: `onTrimMemory()` "to notify an app that memory is running low and that it should reduce its allocations", then LMK, "Background apps are first to be killed". Match.
- Android slow rendering page: "under 16ms to achieve 60 frames per second", system forced to skip frames, "jank". Match.
- Compose `ImageBitmap.readPixels` (IntArray, ARGB values) match, via search of the official page.
- LeetCode: the page fetch for leetcode.com returns 403, so no problem page was opened. Numbers, titles and difficulties of the 14 problems not flagged by the writer (217, 1, 136, 238, 41, 189, 48, 27, 26, 88, 1929, 118, 443, 380) are taken from `docs/practice/practice-dsa.md` (tagged `[both]`) and my own knowledge; I did not re-check them on the web. The four flagged ones were re-checked through web search results that point at the official LeetCode problem URLs and at NeetCode and doocs write-ups: 80 Remove Duplicates from Sorted Array II (Medium), 121 Best Time to Buy and Sell Stock (Easy) and 54 Spiral Matrix (Medium) match; 66 Plus One matches by title and number, and its Easy label comes from a solutions-repo path (`array/easy/`) plus my knowledge. HackerRank Left Rotation and Dynamic Array URLs match `practice-dsa.md`; the HackerRank pages were not opened.
- Android `LruCache`: the AOSP source `frameworks/base/core/java/android/util/LruCache.java` (the reference page is generated from its Javadoc) opens "A cache that holds strong references to a limited number of values." and describes the entry moving to the head of the queue on access and the eldest entry being evicted when the cache is full. The reference page itself was too large for the fetch tool.

**Could not verify (list):** the text of the LeetCode and HackerRank pages; the Android reference page text for `java.util.ArrayList` (confirmed only through search summaries, and the same wording is on the Java SE 21 page); the `LazyListScope.items(count)` signature (page too large; matches my knowledge that `itemContent` receives the index); `ImageBitmap.readPixels` (search summary of the official page only); the `LruCache` reference page text verbatim (see above). None of these changes a lesson claim as written.

## Checklist walk-through

1. Recap: one sentence per line in all four. Pass.
2. Question blocks: time-vs-space 4 sentences, how-arraylist-grows 4, arrays-in-memory 5, insert-and-delete 5; all within the limit. Pass. A few Name it sentences run over 25 words (see FIX 4).
3. Jargon: FIX 4 and 5 list the clear failures. Judged acceptable, not listed: `MutableMap`, key and trailing lambda in time-vs-space Name it (`getOrPut` is explained in the paragraph right after, and the leaf `requires` only Big O, so a stricter reading could ask for a one-clause gloss of `MutableMap` and key); `onTrimMemory`, kernel and process in the Android section (each is glossed in the same sentence); "thread" and "frame" in insert-and-delete and how-arraylist-grows Android sections (glossed by the 16 ms quote and "the thread that draws the screen"). An author who prefers a stricter reading can add one-clause glosses.
4. Labels: see FIX 6.
5. `output` blocks are real stdout and now confirmed by a run. `verified` fields are stale: FIX 1.
6. Keys and `why` lines: pass.
7. No "next lesson", "Coming next" or "on the screen" claims. Pass.
8. Frontmatter equals the roadmap row (title, difficulty, requires, fruit) for all four. Each leaf has one `sim` fence, id in `sims`, and a row in `docs/sims/needed-ids.md` with template. Pass.
9. SOURCES comment present; stray VERIFY marks: FIX 2 and 3.
10. Kotlin compiles (table above). Pass.
11. `## Say it in an interview`: 4 questions each, placed before Recap. Seeds from `practice-dsa.md` used. Pass.
12. Practise lists: 5 problems each, easy to hard, one training line each. Pass.
13. Author-supplied data used (problems, interview questions). `practice-dsa.md` `[B]` problems 1089 and the HackerRank "Arrays - DS" are not used; that is allowed.
14. Sims, hand-traced:
    - `pocket-timetable` (compare): fits "two ways of doing the same thing". Try steps use buttons only; `ask7` x3, `ask12` x2 and optional `fill` are all reachable. Counters, labels and the `bad` mark match the prose. No tokens broken.
    - `locker-row-reach` (lanes): six buttons, no drag. Try 1 `open0, open7`, Try 2 `count0, count7`, optional `open5, count5` all reachable. Step counts 1, 1, 1, 1, 6, 8 match the `say` lines.
    - `cinema-row-shuffle` (lanes): both Try steps reachable. If a learner presses "First person leaves" first, a later second press is refused ("Press Reset to start again"), but a refused event stays in the history and `TryProgress` still counts it, so step 2 still completes.
    - `moving-day-flats` (lanes): 8 cases, `push` into a row lane uses the first free slot, the fifth press moves f1 to f4 to `big.0` to `big.3` then pushes f5, counter reaches 4. Try 1 (4 presses), Try 2 (1), Try 3 (3) add up to 8. Reachable.
    - Several `say` texts carry two or three short sentences; matches earlier accepted leaves (L3-a, L3-b), so not a FIX.
    - No `phone`, `timeline`, drag or text input anywhere. No colour or size in any block.
    - Accessibility: no images, so no alt text needed; all controls are buttons.

## Writer's three flags

- Practise numbers 66, 121, 54, 80: confirmed (above).
- Arrays stem `ai-review` flag: real gap. The `arrays` stem Flags line is `phase=2` while `insert-and-delete-in-an-array` and `how-arraylist-grows` carry `F(ai)` (2 of 3 leaves, meets `docs/08` section 5 "at least half"). See FIX 7. The `strings` stem has the same gap (`text-building-cost` has `F(ai)`); out of scope here.
- LruCache wording: see FIX 3.

Not a FIX, for the owner: (a) The Fix-the-bug answer in time-vs-space uses `clear()` (drops every page, not the least recently used); the explanation already says a real cache drops the least recently used page, so it is honest. (b) The roadmap note about `removeFirst()` / `removeLast()` stays open; no leaf uses them.

## Verdict and FIX list

VERDICT: FIX

1. **`verified` fields, all four leaves (mechanical, `fixer`).** Replace `verified: "UNVERIFIED: compile not run"` with `verified: kotlin-2.4.21` on all 13 challenge blocks: time-vs-space lines 187, 215, 255; arrays-in-memory lines 181, 210, 247; insert-and-delete lines 155, 182, 210, 265; how-arraylist-grows lines 207, 237, 277. Each was compiled and run for this review.
2. **SOURCES comments (mechanical).** Replace the line `⚠️ VERIFY: compile not run for any kotlin block ...` with `Kotlin blocks compiled and run with kotlinc 2.4.21 (scripts/kt-run.sh), 22 Oct 2026.` in time-vs-space line 23, arrays-in-memory line 24, insert-and-delete line 24, how-arraylist-grows line 24. Delete time-vs-space line 24 (LruCache, see 3) and how-arraylist-grows line 25 (the Android `ArrayList` page carries the same wording; add its URL to line 20 as a source).
3. **time-vs-space line 345 (mechanical, text supplied).** Remove the visible `⚠️ VERIFY: exact wording on the class page.` Replace the sentence with: `Android's `LruCache` is a cache that holds a limited number of values. When it is full and a new value arrives, it drops the value used least recently (LRU means least recently used).` Add `(wording checked against the AOSP source Javadoc, android.googlesource.com ... core/java/android/util/LruCache.java)` to SOURCES line 22. Also drop the `⚠️ VERIFY` from the roadmap note once the owner agrees (roadmap edits go through `curriculum-designer`).
4. **how-arraylist-grows, Name it code (mechanical, text supplied).** The code uses `copyOf` and `private set` with no explanation (checklist 3). Add after the code block, before "This prints": `Here `copyOf(n)` makes a new array of length n with the old items copied in; the new spots start as 0. `private set` means only the class itself can change `size` and `copies`.` Also split the 30-word sentence "The Java SE documentation says an `ArrayList` has a capacity, which is ... at least the list size." into two sentences (checklist 2).
5. **arrays-in-memory line 312 (mechanical).** "content lambda" and "Compose" are undefined jargon. Change to: `The Compose `LazyColumn` is Android's scrolling list. Its `items(count)` call takes a block of code that receives the index of the row to draw.`
6. **arrays-in-memory lines 312 to 318 (mechanical).** Label inferences (checklist 4): after "The screen draws only the rows you can see, and for each one it asks for the item at an index." add `(general knowledge)`; after "Pixel number k is one jump away." add `(my own reasoning)`.
7. **`content/dsa/_roadmap.md` line 334 (needs owner / `curriculum-designer`).** Change the arrays stem `Flags: phase=2` to `Flags: phase=2 · ai-review`. Then remove the "Note for the author" line in how-arraylist-grows SOURCES comment (line 26). Same gap exists in the `strings` stem (line 344).
8. **`docs/sims/needed-ids.md` (mechanical).** The 11 new rows (including the four for these leaves) are an uncommitted change (`git status` shows ` M`). Commit them with the fixes.

## Re-check (22 Oct 2026)

- `validateContent`: ok, no warnings.
- Kotlin: the only changed code area (how-arraylist-grows Name it) recompiled with `scripts/kt-run.sh`; prints `Size: 8`, `Items copied in total: 6`. Other blocks had only `verified` field changes, no code changes.
- FIX 1: all 13 `verified: kotlin-2.4.21`. Done.
- FIX 2: SOURCES lines replaced in all four leaves; stale VERIFY lines and the LruCache and Android ArrayList notes removed; Android ArrayList URL added. Done.
- FIX 3: LruCache sentence replaced, no VERIFY left (grep clean), SOURCES line notes the AOSP check. Done.
- FIX 4: `copyOf` / `private set` gloss added; capacity sentence split. Done.
- FIX 5: LazyColumn sentence reworded. Done.
- FIX 6: `(general knowledge)` and `(my own reasoning)` labels added. Done.
- FIX 7: arrays stem now `Flags: phase=2 · ai-review`; author note removed. Done.
- FIX 8: orchestrator commits.

VERDICT (re-check, final): PASS
