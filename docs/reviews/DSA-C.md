# Review DSA-C (22 Oct 2026)

Reviewer: independent. Leaves (all `status: shadow`): `dsa/linked-lists/linked-list-vs-arraylist`, `dsa/stacks/what-is-a-stack`, `dsa/stacks/matching-brackets`.
Checked with `docs/lesson-checklist.md` (14 items), ADR 009, `docs/practice/practice-dsa.md`, `content/dsa/_roadmap.md`.

VERDICT: FIX

## Method
- `../gradlew :site:validateContent` (from `site/`): `validateContent: ok`. No SM0 to SM11 errors, no warnings printed.
- Every Kotlin block (3 Name it, 9 challenge `code`, 3 `fix`) and the Try-it Break and Fix variants were compiled and run with `bash scripts/kt-run.sh` (Kotlin 2.4.21). No compiler warnings. Actual output equals the stated `output:`/`expected:`/prose in every case (table below).
- Sim Try steps were traced by hand against `SimCore.fold` and `TryProgressCalc` (refused events stay in the history, toggles are hoisted). Facts were fetched on 22 Oct 2026 from the official pages.
- NOT done: opening `/preview/<id>` in a browser (mouse, keyboard only, reduced motion, live region). Same gap as L3-a/b/c; owed by the layer build.

## Compile results (actual vs stated)
| Leaf | Block | Actual output | Matches |
|---|---|---|---|
| ll | Name it 1 | hops 5/50/500, values 6/51/501 | yes |
| ll | Name it 2 | `[Ada, Ben, Cy]`, `Cy`, `[Zoe, Ada, Ben, Cy]` | yes |
| ll | follow-three-strings | `value 40 after 3 hops` | yes (key c) |
| ll | slide-the-names | `[Zoe, Ada, Ben, Cy, Dee]`, `slides: 4` | yes (key a) |
| ll | fix before / after | `5050 / 4950` then `5050 / 100` | yes (expected `hops: 100`; explanation 0+...+99 = 4950) |
| ll | Try-it Break (`n - 1`) | hops 9/99/999 | yes |
| ws | Name it | Top plate 3, Taken 3, Taken 2, `Left: [plate 1]` | yes |
| ws | push-three-pop-two | 3, 2, 1 | yes (key b) |
| ws | look-or-take | B, 2, 1 | yes (key c) |
| ws | fix before / after | plate 1,2,3 then plate 3,2,1 | yes |
| ws | Try-it Break | `Taken: plate 1`, then `NoSuchElementException: ArrayDeque is empty.` | yes |
| ws | Try-it Fix (`removeLastOrNull`) | `Taken: null`, no error | yes |
| mb | Name it | six lines true,false,false,false,true,true | yes |
| mb | top-when-closer-arrives | `Top is [` | yes (key a) |
| mb | leftover-opener | `1` | yes (key b) |
| mb | fix before / after | `([)] -> true` (the bug), then `false`; other three lines equal expected | yes |
| mb | Try-it Break (`"([]))"`) | `"([]))" -> false` | yes |

Predict keys are true; correct letters vary (ll c, a; ws b, c; mb a, b); wrong options are plausible and each `why` leads to the option.

## Facts verified (official pages, 22 Oct 2026)
- Java 21 `ArrayList`: size, isEmpty, get, set run in constant time; add amortized constant; the rest linear; "constant factor is low compared to that for the LinkedList". Matches.
- Java 21 `LinkedList`: "Doubly-linked list implementation of the List and Deque interfaces"; indexing "traverse the list from the beginning or the end, whichever is closer". Matches.
- Java 21 `ArrayDeque`: "likely to be faster than Stack when used as a stack, and faster than LinkedList when used as a queue"; `removeLast()` throws `NoSuchElementException` on empty. Matches.
- Java 21 `Stack`: Deque "should be used in preference to this class"; `pop` throws `EmptyStackException`. Matches.
- Kotlin collections overview: default `MutableList` is `ArrayList` ("resizable array"); `ArrayDeque` "fills the role of both a Stack and Queue", "realized using a resizable array". Matches.
- Kotlin `ArrayDeque` reference: since Kotlin 1.4; `last()`, `addLast` members. `removeLast()` throwing and `removeLastOrNull()` returning null confirmed by running them (the reference page was truncated at those entries).
- Android `LruCache`: the developer page text was too long for the fetch tool, but the AOSP `LruCache.java` Javadoc says "Each time a value is accessed, it is moved to the head of a queue" and "the value at the end of that queue is evicted". The source declares `private final LinkedHashMap<K, V> map;`. So the roadmap ⚠️ VERIFY is resolved: yes, built on `LinkedHashMap`. The leaf never states this, which is safe, but see FIX 4.
- Android tasks and back stack: "last in, first out", "pushed on the top of the stack and takes focus", "popped from the top of the stack and destroyed", Android 12+ root launcher goes to background. Matches.
- Android declaring-layout: "exactly one root element", "gradually build a View hierarchy", "in the same way that you create web pages in HTML with a series of nested elements". Matches.
- XML 1.0: "The Name in an element's end-tag MUST match the element type in the start-tag"; "elements ... nest properly within each other". Matches.
- LeetCode numbers and titles: all 22 problem lines equal `docs/practice/practice-dsa.md` (the [both] items are high confidence; the [A]/[B] ones 707, 21, 234, 460, 682, 1047, 232, 225, 42, 1614, 856 match my own knowledge of the titles and difficulty). leetcode.com cannot be fetched (403), so no live page check. HackerRank "Balanced Brackets" slug `balanced-brackets` matches the practice file. Order easy to hard, 7/8/7 items, one training line each: pass.
- Could not verify: the text of the developer.android.com `LruCache` page itself (used the AOSP source instead); LeetCode live pages.

## Sims (ADR 009, checklist 14)
- `index-cards-vs-address-book` (chart), `plate-pile` (lanes), `nested-boxes-check` (lanes): templates fit "Use for"; `sims` frontmatter equals fence ids; ids and template appended to `docs/sims/needed-ids.md`; no colour, size, font or coordinate; no `phone` or `timeline`; no drag, only buttons and one toggle.
- Reachability (traced): chart steps 1 to 3 and optional `big=on, insert-front` reachable. plate-pile: put x3, take-top, pull-middle (always refused, kept in history, counts), optional take-top x3 (third is a refusal on an empty pile) reachable; every take-top case is covered for any push/take order. nested-boxes: bad mode stops at pos 2, `swap-string` then four reads reach `balanced`; step 3 reachable.
- Prose matches: `intro`, "What you should notice", counts in the table. Small mismatch in FIX 6.
- Deviations from roadmap rows (race with lanes; real plates that topple; reuse of the plate sim) are documented in `needed-ids.md`. Accepted.
- Observation, not a FIX: at 1,000 names the chart bar for 1 step is a hairline; the number label and the "show" readout carry the value.

## FIX list
1. All three leaves, 9 `verified:` fields still read `"UNVERIFIED: compile not run"`. The code now compiles and matches. Set `verified: kotlin-2.4.21` on: ll `follow-three-strings` (l.221), `slide-the-names` (l.253), `fix-song-at-in-a-loop` (l.296); ws `push-three-pop-two` (l.198), `look-or-take` (l.229), `fix-pop-from-the-wrong-end` (l.269); mb `top-when-closer-arrives` (l.194), `leftover-opener` (l.224), `fix-counting-checker` (l.261). Mechanical (`fixer`).
2. Stray VERIFY marks (checklist 9): delete ll l.28 and l.29, ws l.29, mb l.28. The roadmap-note lines inside the SOURCES comment (ws l.26, mb l.25) may stay (stripped at go-live). In ll SOURCES line 24 replace "the official page could not be read" with: `Android LruCache: AOSP LruCache.java Javadoc ("moved to the head of a queue", "end of that queue is evicted") and field "private final LinkedHashMap<K, V> map" (read 22 Oct 2026): https://android.googlesource.com/platform/frameworks/base/+/refs/heads/main/core/java/android/util/LruCache.java`. Mechanical.
3. Multi-sentence `say` texts (checklist 14: one plain true sentence; same ruling as L3-b). Merge or trim to one sentence each: ll sim lines 72, 75, 82, 85, 92, 95, 102, 105 (two sentences each); ws sim lines 76 to 120 (about 9 of them), 129 is a `refuse` (two sentences, trim too); mb sim lines 76, 81, 87, 98, 103, 113, 120. Keep every number and fact. Mechanical (`fixer`).
4. ll "Where this shows up in Android", l.391: "Linked structures do live inside libraries" and "Moving one item to the front without sliding the others is the job the cards do well" are unlabelled (checklist 4) and the Android page does not mention a linked structure. Replace the two sentences after the LruCache description with: `The AOSP source of LruCache keeps its values in a LinkedHashMap, which the Java documentation calls a hash table with a linked list running through its entries (general knowledge, Java LinkedHashMap Javadoc: add that URL to SOURCES). My own reasoning: moving one item to the front without sliding the others is the job the cards do well, and it is the part of this lesson called "tying in a card next to one you already hold".` Also define **cache** and **evicted** in plain words before use ("a cache keeps recent answers close; evicted means thrown out to make room"). The fixer must check the LinkedHashMap Javadoc wording. Needs `fixer` with a fetch.
5. Jargon used before it is defined or never bolded (checklist 3). Mechanical (`fixer`), define in one short clause:
   - ll: bold **ArrayList** and **linked list** at first use in Mental model (l.38 to 41), not first at l.149; bold **constant time**, **amortized constant time**, **linear time** and **iterator** where explained in Name it (l.151, l.160); define **ArrayDeque** in one clause where Common mistakes (l.384) first uses it.
   - ws: define **Deque** (a pile or queue open at both ends) before l.174 uses "the `Deque` interface"; in "Where this shows up in Android" (l.357 to 359) define **activity** (one screen of an app), **back stack**, **task** and **root launcher activity** before use, and in Interview Q1 (l.364).
   - mb: **balanced** is used in the sim verdict and Build step 4 before its bold definition in Name it (l.162): add one plain line to the Mental model, e.g. "A line is balanced when every box that was opened gets closed in the right order." Define **View hierarchy**, **tag** and **layout file** in one clause before l.340.
6. ll l.119 "What you should notice" says the cards "lose when you must find a position by walking". The sim shows a clear loss only for "Read the name in the middle"; for "Insert in the middle, by position" it shows 50 vs 51 with "Neither side wins". Change to `...and they lose or tie when you must find a position by walking.` Mechanical.
7. ws l.47 "it grows as long as the program has memory" and "a real pile has a height limit" are unlabelled general claims (checklist 4); append `(general knowledge)`. Also ll l.389: the chat-screen story (50 vs 5,000 messages) is a made-up scenario; start it with "Imagine" so it is not read as a measurement. Mechanical.
8. Style nit, mechanical: ll l.32 first sentence is about 28 words; split at the colon (checklist 2, aim under 25).

No owner decisions needed. After the fixes, `reviewer` re-checks items 1, 3, 4 and 5 only; compile results above stay valid unless Kotlin snippets change.

## Re-check (22 Oct 2026)

Checked: `:site:validateContent` prints `validateContent: ok`. No site Kotlin changed; all Kotlin blocks and challenge snippets of the three leaves were recompiled and run with `bash scripts/kt-run.sh` (kotlinc 2.4.21): no warnings, outputs equal the table above (ws Try-it and linked-list-vs-arraylist numbers unchanged).

- Item 1: PASS. All 9 `verified:` fields read `kotlin-2.4.21`.
- Item 2: PASS. Stray VERIFY marks gone (only the roadmap note in ws SOURCES remains, allowed); ll SOURCES now cite the AOSP LruCache Javadoc and field.
- Item 3: PASS. Every `say` in the three sims is one sentence; the ws middle-pull `refuse` is trimmed to one sentence. Nit: mb `refuse` (l.98) still has two short sentences ("The check has finished. Press ... to start again."); same pattern was accepted in DSA-B, so not blocking.
- Item 4: PASS. ll "Where this shows up in Android" now says the AOSP source keeps LruCache values in a `LinkedHashMap`, cites the Java doc wording, and labels the card-link point "My own reasoning". I fetched the LinkedHashMap Javadoc: "Hash table and linked list implementation of the Map interface ... maintains a doubly-linked list running through all of its entries". Matches. Cache and evicted are defined; the URL is in SOURCES; the chat story starts with "Imagine".
- Item 5: PASS. ll: ArrayList, linked list, constant time, amortized constant time, linear time, iterator, ArrayDeque defined in bold or by clause before use. ws: Deque, activity, back stack, task, root launcher activity and the interview answer define their terms. mb: "balanced" is defined in the Mental model; XML layout file, tags, View hierarchy defined before use.
- Items 6 to 8: PASS (notice wording, "(general knowledge)" labels, split opening).
- Browser pass of the three sims: not done by me (no dev server), owed to the layer build.

VERDICT (re-check, final): PASS
