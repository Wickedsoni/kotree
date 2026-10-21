```yaml
branch: foundation
bough: roots
stems:
  - id: how-computers-work
    title: How Computers Work
    why: A fresher must picture what the machine does before any code makes sense.
    leaves:
      - id: what-does-a-computer-do
        title: What does a computer actually do all day?
        difficulty: easy
        requires: []
        fruit: false
      - id: what-is-a-bit
        title: How can a computer store everything as 0s and 1s?
        difficulty: easy
        requires: [foundation/how-computers-work/what-does-a-computer-do]
        fruit: false
      - id: how-letters-become-numbers
        title: How does a computer store a letter like A?
        difficulty: easy
        requires: [foundation/how-computers-work/what-is-a-bit]
        fruit: false
      - id: what-is-a-cpu
        title: How does the CPU follow instructions one at a time?
        difficulty: easy
        requires: [foundation/how-computers-work/what-does-a-computer-do]
        fruit: false
      - id: memory-vs-storage
        title: Why does a phone have both memory and storage?
        difficulty: easy
        requires: [foundation/how-computers-work/what-is-a-bit]
        fruit: false
      - id: what-is-an-os
        title: Who decides which app gets the screen and the memory?
        difficulty: easy
        requires: [foundation/how-computers-work/what-is-a-cpu, foundation/how-computers-work/memory-vs-storage]
        fruit: false
  - id: first-code
    title: First Code
    why: Writing and running one tiny program removes the fear of the blank editor.
    leaves:
      - id: what-is-code
        title: Why does a computer need such exact instructions?
        difficulty: easy
        requires: [foundation/how-computers-work/what-is-a-cpu]
        fruit: false
      - id: run-your-first-program
        title: Where can I run code without installing anything?
        difficulty: easy
        requires: [foundation/first-code/what-is-code]
        fruit: false
      - id: lines-run-in-order
        title: In what order does a computer run my lines?
        difficulty: easy
        requires: [foundation/first-code/run-your-first-program]
        fruit: false
  - id: programming-thinking
    title: Programming Thinking
    why: Variables, decisions, loops and functions are the building blocks of every program in every branch.
    leaves:
      - id: what-is-a-variable
        title: How does a program remember a value?
        difficulty: easy
        requires: [foundation/first-code/lines-run-in-order, foundation/how-computers-work/memory-vs-storage]
        fruit: false
      - id: what-is-a-type
        title: Why can't I add a word to a number?
        difficulty: easy
        requires: [foundation/programming-thinking/what-is-a-variable, foundation/how-computers-work/how-letters-become-numbers]
        fruit: false
      - id: expressions
        title: How does a program calculate a new value?
        difficulty: easy
        requires: [foundation/programming-thinking/what-is-a-type]
        fruit: false
      - id: yes-or-no-questions
        title: How does a program ask a yes-or-no question?
        difficulty: easy
        requires: [foundation/programming-thinking/expressions]
        fruit: false
      - id: how-a-program-decides
        title: How does a program choose what to do next?
        difficulty: easy
        requires: [foundation/programming-thinking/yes-or-no-questions]
        fruit: false
      - id: how-a-program-repeats
        title: How does a program keep doing something until a condition changes?
        difficulty: easy
        requires: [foundation/programming-thinking/how-a-program-decides]
        fruit: false
      - id: counting-loops
        title: How do I repeat something exactly ten times?
        difficulty: medium
        requires: [foundation/programming-thinking/how-a-program-repeats]
        fruit: false
      - id: what-is-a-list
        title: How do I keep many values under one name?
        difficulty: easy
        requires: [foundation/programming-thinking/what-is-a-variable]
        fruit: false
      - id: loop-over-a-list
        title: How do I do something to every item in a list?
        difficulty: medium
        requires: [foundation/programming-thinking/counting-loops, foundation/programming-thinking/what-is-a-list]
        fruit: false
      - id: what-is-a-function
        title: How do I give a set of steps a name and reuse it?
        difficulty: easy
        requires: [foundation/programming-thinking/what-is-a-variable]
        fruit: false
      - id: function-inputs-and-outputs
        title: How does a function take something in and hand something back?
        difficulty: medium
        requires: [foundation/programming-thinking/what-is-a-function]
        fruit: false
      - id: trace-code-by-hand
        title: How can I know what code prints before I run it?
        difficulty: medium
        requires: [foundation/programming-thinking/loop-over-a-list, foundation/programming-thinking/function-inputs-and-outputs]
        fruit: false
  - id: debugging-mindset
    title: Debugging Mindset
    why: Every program has bugs; a fresher who can find them calmly can learn anything else.
    leaves:
      - id: reading-error-messages
        title: What is an error message trying to tell me?
        difficulty: easy
        requires: [foundation/programming-thinking/what-is-a-type]
        fruit: false
      - id: what-is-a-bug
        title: How can code run without errors and still be wrong?
        difficulty: easy
        requires: [foundation/debugging-mindset/reading-error-messages]
        fruit: false
      - id: find-a-bug-without-guessing
        title: How do I find a bug without guessing?
        difficulty: medium
        requires: [foundation/debugging-mindset/what-is-a-bug, foundation/programming-thinking/trace-code-by-hand]
        fruit: false
      - id: check-code-you-did-not-write
        title: How do I check code that someone else, or an AI, wrote for me?
        difficulty: medium
        requires: [foundation/debugging-mindset/find-a-bug-without-guessing]
        fruit: false
  - id: from-code-to-running
    title: From Code to Running
    why: Knowing how text becomes machine work explains build errors, the JVM and how Android runs Kotlin.
    leaves:
      - id: what-is-a-compiler
        title: How does my code become instructions the CPU understands?
        difficulty: easy
        requires: [foundation/first-code/what-is-code, foundation/debugging-mindset/reading-error-messages]
        fruit: false
      - id: compiler-vs-interpreter
        title: Why do some languages translate everything first and others line by line?
        difficulty: easy
        requires: [foundation/from-code-to-running/what-is-a-compiler]
        fruit: false
      - id: what-is-the-jvm
        title: How can the same Kotlin code run on many different computers?
        difficulty: medium
        requires: [foundation/from-code-to-running/compiler-vs-interpreter]
        fruit: false
      - id: what-is-art
        title: How does an Android phone run Kotlin code?
        difficulty: medium
        requires: [foundation/from-code-to-running/what-is-the-jvm, foundation/how-computers-work/what-is-an-os]
        fruit: false
  - id: how-an-app-is-born
    title: How an App Is Born
    why: The roots end where every Android branch begins, with a real app running on a real phone.
    leaves:
      - id: what-is-an-app-file
        title: What is inside an app file on a phone?
        difficulty: easy
        requires: [foundation/from-code-to-running/what-is-art]
        fruit: false
      - id: what-happens-when-i-build
        title: What happens when I press Build?
        difficulty: medium
        requires: [foundation/how-an-app-is-born/what-is-an-app-file, foundation/from-code-to-running/what-is-a-compiler]
        fruit: false
      - id: how-an-app-gets-installed
        title: How does an app get from a store onto my phone?
        difficulty: easy
        requires: [foundation/how-an-app-is-born/what-happens-when-i-build]
        fruit: false
      - id: what-happens-when-i-tap-an-icon
        title: What happens between tapping an icon and seeing the app?
        difficulty: medium
        requires: [foundation/how-an-app-is-born/how-an-app-gets-installed, foundation/how-computers-work/what-is-an-os]
        fruit: false
      - id: run-your-first-app
        title: How do I run my own first app on a real phone?
        difficulty: easy
        requires: [foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon]
        fruit: false
```

## Branch prerequisites

None. This is the start. Foundation has no `Assumes` of any kind; every term is defined on the page where it first appears (`docs/08` §11). Stems first-code to debugging-mindset use the browser editor (Kotlin Playground) only; Android Studio appears first in the last leaf of the how-an-app-is-born stem.

All 34 leaves are `shadow` on 18 Oct 2026. Nothing is published.

## Stems

### how-computers-work · How Computers Work

Flags: phase=1

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-does-a-computer-do` | easy | "What are the three things a computer does over and over?" (take input, process it, give output) | A kitchen: ingredients come in, the cook works, a dish goes out. | "A computer thinks and understands what I want." | `sim:` a conveyor belt carries input tokens into a box that stamps them and drops output tokens; the learner adds tokens and watches the steps. | `P:` on the sim: which step happens to a keypress first · `B:` remove the "process" box and see that nothing comes out | — |
| `what-is-a-bit` | easy | "How can a row of on/off switches stand for a number or a colour?" | A row of light switches in a hallway: each is on or off, together they make a pattern. | "Computers store real pictures and words inside them." | `sim:` a row of 8 switches with a live counter showing the number they make; flip switches to reach a target number. | `P:` on the sim: which number do switches 0000 0101 make · `B:` add a ninth switch and see how far the count goes | — |
| `how-letters-become-numbers` | easy | "Why does the letter A have a number inside the computer?" | A locker room where every locker has a number and the key tag says which letter sits inside. | "The computer stores the shape of each letter." | `none:` a lookup table is a single rule; a printed table with clickable letters is enough. | `P:` on a table: what number is "B" if "A" is 65 · `B:` swap two table rows and read a word back | Unicode named at overview level only; encoding details stay in `## Later`. |
| `what-is-a-cpu` | easy | "How does the CPU get through a list of instructions?" (fetch one, do it, move to the next) | A cook reading a recipe card line by line, never skipping ahead. | "The CPU does everything at once." | `sim:` a pointer moves down a recipe card one line per tick; the learner speeds up the clock or adds a "jump back" line. | `P:` on the sim: which line runs after line 3 when line 3 says "go to 1" · `B:` set the clock to zero | — |
| `memory-vs-storage` | easy | "Why does unsaved work disappear when the battery dies, but photos stay?" | A desk (memory) versus a filing cabinet (storage): the desk is fast but cleared every night. | "Memory and storage are the same thing; more GB is more GB." | `sim:` items on a desk and in a cabinet; pull the power plug and watch the desk clear while the cabinet keeps its files. | `P:` on the sim: what is left after the plug is pulled · `B:` fill the desk past its edge and watch items fall off | — |
| `what-is-an-os` | easy | "Why can't two apps grab the same memory or the whole screen?" | A building manager who hands out rooms and keys so tenants never walk into each other's flats. | "Apps talk to the hardware directly." | `sim:` three app blocks queue for one screen and one memory shelf; a manager block hands out slots; remove the manager and watch them collide. | `P:` on the sim: which app gets the screen next · `B:` remove the manager | Bridges to OS branch later (no `Assumes` from Foundation). |

### first-code · First Code

Flags: phase=1

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-code` | easy | "Why must code be so exact when a person would understand a sloppy note?" | Giving directions to a very literal robot waiter who does exactly what you say, typos included. | "The computer will figure out what I meant." | `none:` the idea is a single rule about precision; a side-by-side of a vague and an exact instruction is enough. | `P:` which of two instruction lists makes a sandwich with bread on the outside · `B:` remove one step and see what the robot does | — |
| `run-your-first-program` | easy | "Where do I type a program and see it run, with nothing installed?" | Writing a note and pressing a doorbell: you press Run and something answers. | "I need to install big tools before I can write any code." | `none:` tooling steps; the live Kotlin Playground on the page is the demo. | `P:` what does `println("Hi")` print · `F:` a missing quote mark stops the program from running | ⚠️ VERIFY: Kotlin Playground still embeds and runs without login on the publish date. |
| `lines-run-in-order` | easy | "Why does the second line print after the first, and what changes if I swap them?" | A queue at a ticket counter: first in line is served first. | "All lines of a program happen at the same time." | `sim:` a highlight bar steps down a 4-line program and fills an output panel; the learner drags lines to reorder them. | `P:` order of three `println` lines · `F:` lines in the wrong order print a greeting backwards · `B:` move the last line to the top | — |

### programming-thinking · Programming Thinking

Flags: phase=1

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-variable` | easy | "What happens to the old value when I give a variable a new one?" | A labelled jar: the label stays, the contents can be swapped. | "`x = x + 1` is a maths equation that can never be true." | `sim:` labelled jars on a shelf; each line of code drops a new ball into a jar and the old ball falls out. | `P:` value of `x` after three assignments · `F:` a total that never grows because it is reset inside the steps · `B:` read a jar before anything is put in it | — |
| `what-is-a-type` | easy | "Why does the computer refuse to add the word "five" to the number 5?" | Different-shaped containers: a milk bottle does not fit in an egg box. | "A number and text that looks like a number are the same." | `none:` a single rule about kinds of values; the editor error message shows it best. | `P:` what `"5" + 5` gives in Kotlin · `F:` a type mismatch when adding an age read as text | — |
| `expressions` | easy | "Why does 5 / 2 give 2 and not 2.5?" | A shop till that only deals in whole coins and drops the change. | "Division always gives the exact answer." | `none:` a single rule about operators; a table of inputs and results is enough. | `P:` value of `7 / 2`, `7 % 2` and `7.0 / 2` · `F:` average of two whole numbers comes out wrong | Covers operator order and whole-number division; the Kotlin branch builds on it. |
| `yes-or-no-questions` | easy | "What does `age >= 18` turn into when the program runs?" | A turnstile that answers only "yes, go through" or "no, stay". | "`=` and `==` mean the same thing." | `none:` a single rule; a truth table with toggles is enough. | `P:` result of `3 > 2 && 2 > 5` · `F:` a check that uses the wrong comparison and lets 17-year-olds in | — |
| `how-a-program-decides` | easy | "Which branch runs when the condition is false?" | A fork in a garden path with a sign that says "left if raining, right if not". | "Both sides of an if/else run, one after the other." | `sim:` a ball rolls to a fork; a gate flips by the condition and the ball takes one path only; the learner changes the condition. | `P:` which message prints for `temp = 30` · `F:` conditions checked in the wrong order so "hot" is never reached · `B:` remove the `else` | Analogy reused through the stem: paths and gates. |
| `how-a-program-repeats` | easy | "What makes a loop stop?" | Stirring soup until it boils: you check, stir, check again. | "A loop knows by itself when to stop." | `sim:` a ball circles a track and passes a gate each lap; the gate opens when a counter hits the target; the learner breaks the counter to see an endless loop. | `P:` how many times a `while` body runs · `F:` a loop that never ends because the counter is never changed · `B:` change `<` to `<=` | — |
| `counting-loops` | medium | "Which values does `i` take in `for (i in 1..10)`?" | Counting stairs as you climb: you know the first and last step before you start. | "`1..10` stops at 9." | `sim:` reuse `how-a-program-repeats` sim, with a numbered track that shows `i` on each lap and a slider for start and end. | `P:` last value printed by a counting loop · `F:` an off-by-one loop that skips the last step · `B:` count down instead of up | — |
| `what-is-a-list` | easy | "How do I get the third item out of a list?" | A row of numbered post-office boxes, starting at box 0. | "The first item is at position 1." | `sim:` a row of numbered boxes; clicking an index lights the box it reaches; asking for a box past the end shakes the row. | `P:` what `names[1]` prints · `F:` asking for item number 3 in a 3-item list · `B:` ask for index -1 | — |
| `loop-over-a-list` | medium | "How do I add up every number in a list?" | A cashier scanning every item in a basket, one at a time, keeping a running total. | "I have to write one line per item." | `sim:` reuse `what-is-a-list` sim, with a pointer that walks the boxes and a running-total jar that fills. | `P:` total printed for a 4-item list · `F:` running total reset inside the loop · `B:` start the pointer at 1 | — |
| `what-is-a-function` | easy | "Why give a set of steps a name instead of copying them?" | A recipe card you can hand to anyone and say "make this". | "A function runs as soon as I write it." | `sim:` a highlight bar jumps from a call line into a named block and back; the learner adds a second call and sees the jump happen twice. | `P:` how many times "Hello" prints when the function is written once and called twice · `F:` a function that is written but never called | — |
| `function-inputs-and-outputs` | medium | "What is the difference between a function printing a value and returning it?" | A vending machine: coins go in (inputs), a can comes out (output); a light flashing on the front is not the can. | "Printing and returning are the same thing." | `none:` the idea is a contract, not a moving process; a before/after table of calls and results is clearer. | `P:` what `double(double(3))` gives · `F:` a function that prints the answer but returns nothing, so the caller gets no value · `B:` swap the two parameters | — |
| `trace-code-by-hand` | medium | "How do I predict the output by writing down each variable's value line by line?" | Following a treasure map step by step with a pencil, marking where you are. | "Tracing is only for slow people; real programmers just know." | `sim:` a table fills row by row as a highlight bar steps through a loop that calls a function; the learner predicts the next row before it appears. | `P:` predict a 6-line program with a loop and a function · `F:` trace reveals a variable updated in the wrong place | Synthesis leaf for the stem. |

### debugging-mindset · Debugging Mindset

Flags: phase=1 · ai-review

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `reading-error-messages` | easy | "Which line and which word of an error message tell me where to look?" | A car dashboard warning light: it names the system and points you to the right page of the manual. | "An error message means I broke the computer." | `none:` reading text is a single skill; real error messages from the Playground are the material. | `P:` which line number the error points to · `F:` fix the line the message names (missing bracket) | Uses real Kotlin Playground messages. ⚠️ VERIFY: message wording on the publish date. |
| `what-is-a-bug` | easy | "Why can a program run without any error and still print the wrong answer?" | A recipe that says "1 kg of salt" instead of "1 g": the cook follows it perfectly and the dish is ruined. | "If there is no red error, the code is correct." | `none:` a single idea about kinds of failure; a three-column table (will not run / crashes / wrong answer) is clearer. | `P:` which of three snippets runs but prints a wrong total · `F(ai):` a neat, commented average function that divides by the wrong count | — |
| `find-a-bug-without-guessing` | medium | "How do I narrow a bug down to one line instead of changing things at random?" | Finding a blown fuse by switching rooms off one half at a time. | "Debugging means changing things until it works." | `sim:` `fuse-halving-search`: a row of 16 rooms on one fuse with one hidden faulty lamp; the learner switches on a chosen block of rooms, the fuse blows or holds, and a counter shows the tries; halving finds the lamp in 4 tries; the learner can move the lamp and retry. | `P:` on the sim: how many checks to find the leak in 16 segments · `F(ai):` a tidy loop with an off-by-one bug found by adding one print · `B:` remove the print and try to guess | — |
| `check-code-you-did-not-write` | medium | "What should I check before I trust a piece of code that looks correct?" | Checking a hired builder's work: the paint looks fine, but you still test the light switches and the taps. | "If the code looks clean and has comments, it must be right." | `none:` a checklist skill (read the goal, trace one input, try an edge case); the editor is enough. | `F(ai):` a confident, well-commented "find the largest number" function that checks conditions in the wrong order · `F(ai):` a loop that skips the first item · `P:` what the code prints for an empty list | Framing: "Would you accept this?" (`docs/01` §3.5). Never "AI is bad". |

### from-code-to-running · From Code to Running

Flags: phase=1

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-a-compiler` | easy | "Why does the compiler complain before my program even starts?" | A translator who reads your whole letter first and refuses to translate a sentence that makes no sense. | "The computer runs my Kotlin text exactly as I typed it." | `sim:` a text card goes into a translator box and comes out as a stack of small instruction tiles; a broken card is stopped at the door with a red note. | `P:` on the sim: does a card with a typo reach the CPU · `F:` fix the line that stops compilation | — |
| `compiler-vs-interpreter` | easy | "Why do some languages check the whole program first while others stop halfway?" | A translator who translates a whole book before printing, versus an interpreter who speaks each sentence live. | "Kotlin is read and run line by line, like a script." | `sim:` reuse `what-is-a-compiler` sim, with a second lane where cards are translated one at a time; a bad card halfway shows where each lane stops. | `P:` on the sim: in which lane do the first two lines still run | — |
| `what-is-the-jvm` | medium | "Why does Kotlin first become bytecode instead of instructions for one exact CPU?" | A universal power adapter: one plug shape, and an adapter in each country makes it fit the wall. | "The JVM is a programming language." | `sim:` one bytecode tile stack feeds into three different machine boxes, each with its own adapter; remove an adapter and that machine cannot run it. | `P:` on the sim: which machines can run the stack after one adapter is removed | Overview level only. |
| `what-is-art` | medium | "What runs my Kotlin code on an Android phone, if not the JVM?" | The same recipe cooked in a different kitchen with its own staff: same dish, different cooks. | "Android phones run the JVM, the same as a laptop." | `sim:` an animation of bytecode turning into a phone-sized package and being run by the phone's own runtime box; a toggle shows "translate ahead" versus "translate while running". | `P:` on the sim: which box runs the app on the phone | Checked 18 Oct 2026, source.android.com "Configure ART": since Android 7, ART mixes ahead-of-time compilation, just-in-time compilation and interpretation, with profile-guided compilation. ⚠️ VERIFY again before live; keep DEX and ART at overview level. |

### how-an-app-is-born · How an App Is Born

Flags: phase=1 · volatile

| Leaf | Diff | After this, a fresher can answer | Analogy | Misconception | Sim | Challenges | Notes |
|---|---|---|---|---|---|---|---|
| `what-is-an-app-file` | easy | "What is packed inside the file that becomes an app?" | A flat-pack furniture box: parts, screws, a picture and an instruction sheet, all in one sealed box. | "An app is one single program file with nothing else in it." | `sim:` an animation unpacks a box into code, pictures, text and a label card (the manifest); the learner removes the label and the phone refuses the box. | `P:` on the sim: which part tells the phone the app's name and icon | Names APK, AAB and manifest at overview level. ⚠️ VERIFY: Google Play requires new apps to upload an Android App Bundle (since Aug 2021, Play Console Help, checked 18 Oct 2026); phones still install APKs. Re-check before live. |
| `what-happens-when-i-build` | medium | "What steps turn my project into an installable app file?" | A bakery line: mix the dough, bake it, box it, seal the box with the shop's stamp. | "Build just copies my code onto the phone." | `sim:` an animation of a conveyor belt with stations (compile, convert, pack, sign); the learner removes the sign station and the phone rejects the box. | `P:` on the sim: what happens to a box that is not signed · `B:` remove the compile station | ⚠️ VERIFY: build steps named against developer.android.com "Configure your build" overview on the publish date. No Gradle details (that is the Tools branch). |
| `how-an-app-gets-installed` | easy | "How does an app travel from a store to my phone?" | Posting a sealed parcel: the shop checks it, the post office delivers it, you open it at home. | "The store sends my exact project folder to every phone." | `sim:` an animation of a sealed box going from a store shelf through a check to a phone, which unpacks it into its own locked room. | `P:` on the sim: what the phone checks before it opens the box | ⚠️ VERIFY: Play delivery from an App Bundle (split APKs per device) on the publish date. |
| `what-happens-when-i-tap-an-icon` | medium | "What does the phone do in the second between my tap and the first screen?" | Ringing a hotel bell: the manager finds a free room, wakes the staff and only then opens the door. | "The app is always running in the background, waiting for my tap." | `sim:` an animation timeline: tap, the system starts a new process, the app's code loads, the first screen draws; a slider slows each step. | `P:` on the sim: which step comes before the first screen appears | Overview only; process start and lifecycle are taught in Android core. ⚠️ VERIFY: wording of the start-up steps against developer.android.com "App startup time". |
| `run-your-first-app` | easy | "How do I get my own app running on my own phone?" | Planting your first seed and watching the first sprout break the soil. | "You need a paid account or a store to put an app on your own phone." | `none:` tooling steps on real software; a short reference video and annotated screenshots show it. | `P:` which setting must be on before the phone accepts an app from your computer · `F:` a "device not found" checklist with one step missed | Foundation finale (`docs/08` §12 F1). First use of Android Studio. ⚠️ VERIFY: Android Studio new-project template name, developer options and USB or wireless debugging steps on the publish date. |

## Fruits

None. Foundation has no fruit (`docs/08` §12 F1). Its finale is the last leaf of the file: run your first app on a phone.

## Later

- Binary arithmetic in depth (two's complement, floating-point layout): not needed to write a first program.
- Text encodings in depth (UTF-8 bytes, emoji, surrogate pairs): the overview leaf on letters as numbers is enough to start.
- How a CPU pipeline and cache work: interesting, but no fresher task depends on it.
- History of computing: nice context, no skill depends on it.
- Logic gates and circuits: below the level any branch needs.

## Roadmap changes

| Date | Change | Leaf-count delta | Reason |
|---|---|---|---|
| 2026-10-18 | First full roadmap: 6 stems, 34 leaves, all `shadow`. | +34 | Inside the 26–38 band (estimate 32). Stem 3 has 12 leaves (sketch 11) because "repeat until" and "repeat exactly n times" are two mechanisms; stem 6 has 5 (sketch 4) because the finale "run your first app on a phone" needs its own leaf. |
| 2026-10-18 | Sim share note (A2e review): 23 of 34 leaves have a `sim:` cell (68 %), above the 15–25 % guide in `docs/08` §6. Kept because Foundation teaches memory, order and repetition to people who have never run code, and a ball or a lift they can push is the cheapest way to see them; `physics-designer` should check which of them can share one kit before `docs/05` sizes the workload. | 0 | Logged, no leaf cut. |
