# Review W6-b: eight shadow leaves (strings, collections)

Date 20 Oct 2026. Reviewer: independent (did not write the leaves). Gate: L31/L33. Voice: L29. Nothing in the leaves was edited.
Checked against `docs/lesson-checklist.md` (items 1 to 11; item 12 is DSA/DAA/OS only, n/a), `content/_templates/leaf.md`, `docs/04`, `docs/06` §5, hard rules, `content/kotlin/_roadmap.md` rows (lines 130-182 and 468-490), kotlinlang.org and developer.android.com pages read 20 Oct 2026 (raw HTML for the Kotlin docs pages, so quotes are exact).

VERDICT: FIX (0 PASS, 8 FIX). Every FIX is small and mechanical (`fixer` can do all of them). No factual error was found in any claim. No leaf fails on code.

| Leaf | Verdict | Items |
|---|---|---|
| `kotlin/strings/strings-are-sequences` | FIX | 2 (answer length, verified) |
| `kotlin/strings/everyday-string-functions` | FIX | 2 (undefined "mutable", verified) |
| `kotlin/strings/text-to-numbers` | FIX | 2 (`Double` not bold, verified) |
| `kotlin/collections/list-vs-mutable-list` | FIX | 2 ("interfaces" and "extends" undefined, verified) |
| `kotlin/collections/index-out-of-bounds` | FIX | 2 (idiomatic last-item answer, verified) |
| `kotlin/collections/sets` | FIX | 2 (unlabelled general-knowledge claim, verified) |
| `kotlin/collections/maps` | FIX | 2 (answer far too long to say aloud, verified) |
| `kotlin/collections/pairs-and-destructuring` | FIX | 2 (name-based destructuring now in the docs, verified) |

## How I checked code

Pinned compiler: `bash scripts/kt-run.sh` (kotlinc 2.4.21). I extracted every `kotlin` block (14) and every Predict `code` block (24) into files and compiled and ran all 38, then diffed stdout against each `output` block. Plus 5 probe programs for claims in the interview answers and Common mistakes.

- All 24 Predict `output` blocks equal real stdout byte for byte. Every `correct: true` option matches its output. No compiler warnings on any program.
- Key letters vary in every leaf (see the table below; no leaf uses one letter three times). All wrong options are plausible and each `why` leads to the option.
- All 4 `// does not compile` blocks really fail (K2 messages): strings-are-sequences "no 'set' operator method providing array access"; list-vs-mutable-list "unresolved reference 'add' on receiver of type 'List<String>'"; sets "no 'get' operator method providing array access"; pairs "destructuring of type 'Pair<String, Int>' requires operator function 'component3()'".
- The 4 "compiles but stops" programs behave as stated: `rooms[2]` (ArrayIndexOutOfBoundsException, prints `asking` only), `getValue("juice")` (NoSuchElementException "Key juice is missing in the map."), `"abc".toInt()` (NumberFormatException, prints `before` only), `code[code.length]` on a String (StringIndexOutOfBoundsException).
- Common-mistake claims that I also ran: `prices["tea"] + 1` fails ("operator call is prohibited on a nullable receiver of type 'Int?'"); `val numbers = mutableListOf(..); numbers = ...` fails ("'val' cannot be reassigned"); `var list = listOf("a"); list.add("b")` fails ("unresolved reference 'add'"); `getOrNull(5)!!`, `s.uppercase()` with the result dropped compile without a warning.

### Real results, per Predict block (all 24 ran on 2.4.21)

| Leaf | Block id | Real stdout | Key |
|---|---|---|---|
| strings-are-sequences | length-and-last-bead | `5` / `o` | c |
| | last-bead-by-length | `s` / `P` | a |
| | tag-equal-to-length | `start` then exception (stderr) | b |
| everyday-string-functions | copy-not-filed | `hello` | a |
| | trim-then-split | `3` / `[green ]` | b |
| | replace-every-copy | `a+b+c` / `a-b-c` | c |
| text-to-numbers | slip-then-stand-in | `-1` / `12` | c |
| | spaces-on-the-note | `null` / `42` | b |
| | empty-note-alarm | `start` then NumberFormatException | a |
| list-vs-mutable-list | val-hook-still-writes | `2` / `[green, black]` | c |
| | cover-sees-the-change | `2` | b |
| | photo-stays-old | `2` / `3` | a |
| index-out-of-bounds | clerk-with-stand-in | `Ben` / `none` | b |
| | door-number-equals-count | `null` / `Cy` | c |
| | three-from-the-end | `before` then ArrayIndexOutOfBoundsException (index -1) | a |
| sets | size-of-repeated-values | `3` / `[1, 2, 3]` | c |
| | red-is-not-red | `3` | b |
| | order-for-sets-and-lists | `true` / `false` | a |
| maps | missing-ticket-stand-in | `4` / `0` | b |
| | add-to-the-count | `2` / `1` / `2` | c |
| | same-ticket-twice | `2` / `green` | a |
| pairs-and-destructuring | walk-the-shelf | `Ana is 31` / `Ben is 28` | b |
| | swapped-plate-labels | `3 costs tea` | c |
| | bin-in-the-middle | `zx` | a |

The 14 body `kotlin` blocks print exactly what the text under each says (strings k1 `6 K n`; everyday `[  Sam  ]`, `[Sam]`, `SAM`, `Som`, `true`; text-to-numbers `-1 43 42 null`; list-vs `4 cake 4 5`; index `5 Moon null "no such room" Sun`; sets `3 true false true`; maps `3 null 0 5 3 4`; pairs `tea 3 tea x "tea costs 3" "juice costs 6"`).

Probe facts that back the interview answers (all run on 2.4.21): `"😀".length` is 2; `"".split(",")` has size 1 (one empty string); `setOf(3,1,2)` prints `[3, 1, 2]` and its class is `LinkedHashSet`; `hashSetOf("banana","apple","cherry","date","elderberry")` prints `[banana, date, apple, cherry, elderberry]` (not insertion order); `toSet()` and `distinct()` keep order; `mapOf(1 to "a", 2 to "b", 1 to "c")` is `{1=c, 2=b}`; `mutableMapOf` is a `LinkedHashMap`; `emptyList().lastIndex` is -1 and `getOrNull(-1)` and `lastOrNull()` are `null`; `val (email, username) = User("u","e")` gives `email = "u"` (position-based by default); `"٤٢".toIntOrNull()` is `42` (see note under text-to-numbers).

## Fact check (official pages, read 20 Oct 2026)

| Claim | Page | Result |
|---|---|---|
| Index starts at zero, exception outside the range; String immutable, "any operation that appears to modify the string actually creates a new string"; `+` creates a new String per operation, "may not be beneficial in loops"; `buildString()`/`StringBuilder`; templates "easier to read and more idiomatic" | kotlinlang.org/docs/strings.html | Matches |
| `Char` is a UTF-16 code unit; outside the BMP is a surrogate pair; `"🥦".length` is 2; "use Unicode code points stored as `Int`" | kotlinlang.org/docs/characters.html | Matches. The page does not say "length counts code units"; the leaf says "My own reasoning" at the right place |
| `CharSequence.getOrNull(index): Char?` returns the char or `null` if out of bounds | kotlin.text/get-or-null.html | Matches |
| `toInt()` and `toIntOrNull()`: optional `+`/`-`, decimal digits (0-9), `Int` range; samples `042`, `-42`, `2147483648`, `-1a`, `1_00`, `" 22 "` give null; the first seven table rows are on the page | kotlin.text/to-int.html, to-int-or-null.html | Matches. The 8th row (`"abc"`, `""`) is labelled own reasoning |
| `trim()` "leading and trailing whitespace removed"; `replace` "all occurrences"; `substring` start inclusive, end exclusive; `contains`/`startsWith` `ignoreCase = false` by default | kotlin.text/* | Matches |
| `split`: no delimiter gives a single element; ends with delimiter gives trailing empty string; `"".split("sep") == listOf("")` | kotlin.text/split.html | Matches |
| Pair of interfaces; mutable collection allowed under `val`, reassigning is a compile error; read-only types covariant (Rectangle/Shape), mutable ones are not "otherwise runtime failures"; List "specified order", indexed from zero to `lastIndex`; Set "unique elements, order generally undefined"; Map keys unique, equal values allowed | kotlinlang.org/docs/collections-overview.html | Matches. The page never says "immutable", only "read-only" |
| `sourceList`/`referenceList: List<Int>`, "shows the current state of sourceList"; `toList()` "snapshot ... won't affect the copies"; copies are shallow | kotlinlang.org/docs/constructing-collections.html | Matches |
| Index access: "If the list size is less than the specified index, an exception is thrown"; `getOrNull`; `elementAt` throws IndexOutOfBoundsException (3-item sample); `lastIndex` "-1 if the list is empty" | list-operations.html, element-at.html, last-index.html | Matches |
| `Set` "does not guarantee any particular order"; `setOf`/`mutableSetOf` "iterated in the order they were specified"; `setOf(1,2,3) == setOf(3,2,1)` true; `HashSet` "Do not rely on any specific iteration order"; `LinkedHashSet` insertion order, re-insert does not change it; `toSet()` and `distinct()` keep order | kotlin.collections `-set`, `set-of`, `mutable-set-of`, `-hash-set`, `-linked-hash-set`, `to-set`, `distinct` | Matches |
| `map[key]` is `null` if missing; `getValue` throws `NoSuchElementException`; `mapOf` iterates in specified order, last pair wins for a repeated key; `put`/`[]=` overwrite; `HashMap` "fast lookups based on keys", relies on `hashCode`/`equals`, "violating this contract can lead to incorrect behavior", key must stay unchanged or the entry "may become unreachable", no order guarantee, `LinkedHashMap` for insertion order | map-operations.html, map-of.html, get-value.html, `-hash-map`, `Any.hashCode` | Matches |
| `val (a, b) = x` compiles to `component1()`/`component2()`; `componentN()` must be `operator`; data classes declare them; `_` skips (not called); `for ((key, value) in map)`; Pair: "no meaning attached", equal if both components equal; "often better to have your data named properly"; `val (email, username) = user` swaps | destructuring-declarations.html, `-pair`, `-triple` | Matches. The same page has a new section "Name-based destructuring" (Experimental). See pairs FIX 1 |
| Compose state: caution that `ArrayList<T>`/`mutableListOf()` as state "causes your users to see incorrect or stale data", not observable, no recomposition, use `State<List<T>>` and "the immutable `listOf()`"; `val (value, setValue) = remember { mutableStateOf(default) }` is one of three "equivalent" forms; the page has no word "destructur" | developer.android.com/develop/ui/compose/state | Matches |
| `rememberPagerState(pageCount = { 10 })`, `// Display 10 items`, `HorizontalPager(state = pagerState) { page -> ...` | .../compose/layouts/pager | Matches |
| `selectedItems` set of `Int` in `rememberSaveable { mutableStateOf(setOf<Int>()) }`, `selectedItems -= index` / `+= index`, "Selected ... items" with `.size` | .../compose/components/app-bars-dynamic | Matches |
| `groupedMenu[true] ?: listOf()`, "Because the result of subscripting a `Map` is nullable, you can use the Elvis operator" | codelabs/basic-android-kotlin-compose-higher-order-functions | Matches |
| `Map<Char, List<Contact>>` on the lists page; `val (name, age) = mary`; `val (image, title, subtitle) = createRefs()` | .../compose/lists, .../compose/kotlin | Matches |
| Tip calculator: `mutableStateOf("")`, `tipPercent` with `toDoubleOrNull() ?: 0.0`, "could be null if the text field is empty" | codelabs/basic-android-kotlin-compose-calculate-tip | Matches |
| `account.name?.trim()` and the "trim ... leading or trailing whitespace" sentence | developer.android.com/kotlin/common-patterns | Matches |

Claims I could not verify against an official page: none that the leaves present as official. The "General knowledge" and "My own reasoning" labels are on every inferred point I found, with the exceptions listed in the FIX items (sets FIX 1).

## Structure and checklist (all eight)

- Frontmatter keys and order, `id` = path, `title`, `difficulty`, `requires`, `fruit` equal the roadmap rows (`content/kotlin/_roadmap.md` lines 134-177). `order` 22 to 29 follows 21 (`when-to-use-double-bang`). `status: shadow`, `video: ""`, `playground: false` with every Predict `run: false` (accepted W2 to W5 pattern; allowed by CH10). `./gradlew :site:validateContent` (run once): `validateContent: ok`, BUILD SUCCESSFUL.
- Heading order is the template order minus Try it and Fix the bug (playground false) and, for the six leaves with `sims: []`, minus See it move. `## Say it in an interview` sits after "Where this shows up in Android" and before "Recap" in all eight, with 4 questions each. One SIM marker equal to `sims` in the two sim leaves; both ids are in `docs/sims/needed-ids.md` (lines 32-33).
- SOURCES comment at line 18 in all eight; no VERIFY mark, no "next lesson", no "Coming next", no "the author is learning". Question blocks are 4 or 5 sentences. Recaps are three one-sentence lines under 20 words without semicolons (longest 19 words, `index-out-of-bounds` line 236). Tricks 4 or fewer; Common mistakes 3 or 4; You can now 3 lines each starting with a verb. First use of the Name-it terms is bold.
- Item 4 labels: present on every "tip calculator", "badge", "screen" inference ("This next part is our own reasoning").
- Hard rules: no hardcoded colours or fonts (content only), no L26 sections, no Video heading.

## Rulings

1. **Sim divergences (`menu-board-cover`, `inn-room-clerk`): acceptable, not a rule 5 violation.** Same ruling as W4-b and W5-b. Rule 5 protects structure (id, title, difficulty, requires, fruit; build rule V5), and all eight leaves equal their rows. The roadmap Sim and Analogy cells are notes. `list-vs-mutable-list` uses a menu board with a cover instead of "printed menu versus whiteboard"/"two shelves behind glass", and `index-out-of-bounds` builds a new inn sim instead of reusing the `what-is-a-list` sim; both keep ONE analogy per leaf (docs/04 §3.2) and both are recorded in `needed-ids.md`. Owner follow-up, not blocking: `curriculum-designer` rewords the Sim cells of roadmap lines 480-481 so `sim-engineer` builds to the leaf text (a new sim costs more than the reuse the roadmap planned).
2. **Roadmap `F(ai):` and `B:` challenges not delivered as challenge blocks** (list-vs-mutable-list F(ai)/B, sets F, maps F(ai), everyday B, pairs and others are covered only as Common mistakes or the does-not-compile block): acceptable under the accepted `playground: false` pattern (W2 to W5); the key misconceptions are all tested by a Predict. No FIX.
3. **Interview section quality (L33):** all 32 answers are accurate; labels are placed correctly; the "read-only view, not immutable" answer, the HashSet/LinkedHashSet ordering answers, the HashMap `equals`/`hashCode` contract and the `toIntOrNull` rules all match the pages above. UTF-16 claims are grounded in the characters page. The only issues are length (two answers) and the items below.

## FIX lists

### 1. `content/kotlin/strings/strings-are-sequences.md`

1. Line 207: interview answer is 6 sentences and 91 words; the template says 2 to 4 short sentences, easy to say aloud (L29). Replace the whole line:
   old: `No. The Kotlin characters page says a `Char` is one UTF-16 code unit, which is one 16-bit piece of how text is stored as numbers. A symbol outside the basic range, such as an emoji, is stored as two `Char` values called a surrogate pair. The page shows an emoji with a length of 2. My own reasoning: an app that cuts a name at 10 characters could cut an emoji in half. The page says to use Unicode code points, stored as `Int`, to handle such symbols one by one.`
   new: `No. A `Char` is one UTF-16 code unit, a 16-bit piece of how text is stored, and an emoji takes two of them, called a surrogate pair. The Kotlin characters page shows an emoji with a length of 2, and says to use Unicode code points, stored as `Int`, to handle such symbols one by one. My own reasoning: an app that cuts a name at 10 characters could cut an emoji in half.` (mechanical)
2. Lines 102, 129, 156: `verified: not-run` → `verified: kotlin-2.4.21` (all three blocks ran on 2.4.21 and match; `run: false` stays). (mechanical)

### 2. `content/kotlin/strings/everyday-string-functions.md`

1. Line 204 (checklist item 3): "mutable" is used before any lesson defines it (`list-vs-mutable-list` is order 25, this leaf is order 23).
   old: `The documentation describes `StringBuilder` as a mutable sequence of characters for efficient string operations, and says`
   new: `The documentation describes `StringBuilder` as a mutable sequence of characters, which means text you can change in place, built for efficient string operations, and says` (mechanical)
2. The three `verified: not-run` lines (101, 127, 155) → `verified: kotlin-2.4.21`. (mechanical)

### 3. `content/kotlin/strings/text-to-numbers.md`

1. Line 207 (checklist item 3): `Double` is defined but not bold at first use.
   old: `A `Double` is a number with a decimal part`
   new: `A **`Double`** is a number with a decimal part` (mechanical)
2. The three `verified: not-run` lines (114, 142, 169) → `verified: kotlin-2.4.21`. (mechanical)

Note, no change required: the leaf correctly attributes "decimal digits (0-9)" to the documentation. On the JVM, `"٤٢".toIntOrNull()` really returns `42` (Arabic-Indic digits are accepted), so the documented rule is slightly narrower than the behaviour. Owner may add one sentence to Q2 later; I did not find an official page that states it, so it must not be added as a documentation claim.

### 4. `content/kotlin/collections/list-vs-mutable-list.md`

1. Line 75 (checklist item 3): "interfaces" and "extends" are unexplained jargon (the interface lesson comes later).
   old: `each collection type comes as a pair of interfaces: a read-only one for accessing the elements, and a mutable one that extends it with write operations such as adding, removing and updating.`
   new: `each collection type comes as a pair of types, which Kotlin calls **interfaces**: a read-only one for accessing the elements, and a mutable one that has everything the read-only one has and adds write operations such as adding, removing and updating.` (mechanical)
2. The three `verified: not-run` lines (126, 154, 181) → `verified: kotlin-2.4.21`. (mechanical)

### 5. `content/kotlin/collections/index-out-of-bounds.md`

1. Lines 231 to 232: the answer works but a senior interviewer expects `lastOrNull()`; verified: `List.lastOrNull()` "Returns the last element, or `null` if the list is empty" (kotlin.collections/last-or-null.html) and the run printed `null` for an empty list.
   old: `Use `list.getOrNull(list.lastIndex)`. The documentation says `lastIndex` is -1 for an empty list, and -1 is out of bounds, so `getOrNull` returns `null` instead of crashing.`
   new: `Use `list.lastOrNull()`, which the documentation says returns the last element, or `null` if the list is empty. The longer form `list.getOrNull(list.lastIndex)` gives the same result, because the documentation says `lastIndex` is -1 for an empty list, and -1 is out of bounds.` (mechanical)
2. The three `verified: not-run` lines (124, 151, 178) → `verified: kotlin-2.4.21`. (mechanical)

### 6. `content/kotlin/collections/sets.md`

1. Line 207 (checklist item 4): "quick answers" is a general CS claim without a label, and the `Set` interface alone does not promise speed.
   old: `Use a `Set` when you need unique values and quick answers to "is it here?". Use a `List` when order, numbered places or repeats matter. The documentation says a `Set` holds unique elements, while a `List` gives indexed access in a specified order.`
   new: `Use a `Set` when you need unique values and often need to ask "is it here?". Use a `List` when order, numbered places or repeats matter. The documentation says a `Set` holds unique elements, while a `List` gives indexed access in a specified order. General knowledge: a hash-based set can answer "is it here?" without checking every item, while a list checks them one by one.` (mechanical)
2. The three `verified: not-run` lines (103, 130, 158) → `verified: kotlin-2.4.21`. (mechanical)

### 7. `content/kotlin/collections/maps.md`

1. Line 229: the answer is 5 sentences and 126 words, with "it also says" pointing at an unclear subject; too long to say aloud (template: 2 to 4 short sentences). Replace the whole line:
   old: `The documentation for `hashCode` says that if two objects are equal according to `equals`, they must give the same hash code, ... My own reasoning: this is why text and numbers, which never change, make good keys.` (the full line 229)
   new: `The documentation for `hashCode` says that if two objects are equal according to `equals`, they must give the same hash code, which is a number computed from the object. The `HashMap` page says it relies on both functions to locate entries, and that breaking the contract can lead to incorrect behavior. My own reasoning in coat-check words: the clerk picks a section by the number, so two equal tickets with different numbers are looked for in different sections and the coat is missed. The page also warns that changing a key after use can make its entry unreachable, so my own reasoning is that text and numbers, which never change, make good keys.` (mechanical)
2. The three `verified: not-run` lines (118, 145, 177) → `verified: kotlin-2.4.21`. (mechanical)

### 8. `content/kotlin/collections/pairs-and-destructuring.md`

1. Line 221 (accuracy, senior level): "By position." is right for ordinary `val (a, b)` and I confirmed it on 2.4.21, but the official destructuring page now has a section "Name-based destructuring" (Experimental, needs `-Xname-based-destructuring`; without the flag the compiler on 2.4.21 refuses the new syntax). An interviewer in 2026 may ask. Append one sentence:
   old: `So the order of your names must match the order of the properties.`
   new: `So the order of your names must match the order of the properties. A name-based form exists, but the documentation marks it Experimental and it needs a compiler option.` (mechanical)
2. The three `verified: not-run` lines (120, 148, 174) → `verified: kotlin-2.4.21`. (mechanical)

## Optional, owner level (not blocking)

- Add one interview line to `list-vs-mutable-list` Q1 on true immutable lists for Compose (library `kotlinx.collections.immutable`). I did not verify that library in this review, so a writer must check the official page first.
- Reword roadmap Sim cells as in Ruling 1.
