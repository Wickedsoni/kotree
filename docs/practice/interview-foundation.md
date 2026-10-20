# interview: foundation

## foundation/how-computers-work/what-does-a-computer-do
interview-questions:
- [intern] What are the three fundamental operations that every computer processor performs billions of times per second?
- [junior] How do hardware components (CPU, RAM, storage, and I/O) interact when you open an app on your phone?
- [mid] Why does von Neumann architecture bottleneck data throughput between the CPU and main memory, and how do modern mobile CPUs mitigate this?
- [senior] What goes wrong in production when an app's working memory footprint exceeds physical RAM, causing aggressive OS paging and kernel thrashing?
what-interviewers-listen-for:
- CPUs perform arithmetic/logic, data movement (registers to memory), and conditional branching.
- Memory hierarchy (L1/L2/L3 cache, RAM, flash storage) introduces latency trade-offs between speed and capacity.
- L1/L2/L3 CPU caches and hardware prefetchers mitigate memory bus bottlenecks, but cache misses stall instruction pipelines.
build-exercise: Write a nested loop in Kotlin traversing a large primitive array either row-wise (cache-friendly) or column-wise (cache-unfriendly), and measure execution time differences. Observe how memory access patterns affect CPU execution speed.
official-references:
- Android performance overview | https://developer.android.com/topic/performance
common-wrong-beliefs:
- A computer processor computes complex math equations directly in a single step | CPUs only perform basic binary arithmetic (addition, bitwise logic) broken down from high-level instructions.

## foundation/how-computers-work/what-is-a-bit
interview-questions:
- [intern] What is a bit, and why do computers use binary (0s and 1s) instead of decimal numbers?
- [junior] How do eight bits combine to form a byte, and what is the maximum unsigned integer value a single byte can hold?
- [mid] How do bitwise operators (`and`, `or`, `shl`, `shr`) optimize flag checks and memory usage in low-level Android code?
- [senior] What goes wrong in production when bit-shift or integer overflow occurs in arithmetic bounds calculations (e.g., buffer sizing or canvas dimensions)?
what-interviewers-listen-for:
- A bit is a binary digit representing a physical voltage state (off/on, low/high). 8 bits make a byte ($2^8 = 256$ values).
- Bitwise flags allow packing multiple boolean states into a single integer bitmask.
- Integer overflow wraps around silently in Kotlin/Java unless checked or using `toLong()`, causing out-of-bounds errors or security vulnerabilities.
build-exercise: Define an integer bitmask with multiple flag constants (e.g., READ = 1, WRITE = 2, EXECUTE = 4). Combine them using bitwise OR, check permissions using bitwise AND, and observe how multiple booleans compress into one integer.
official-references:
- Basic types | https://kotlinlang.org/docs/basic-types.html
common-wrong-beliefs:
- Computers understand decimal numbers natively | Hardware circuits operate entirely on electrical voltage levels representing binary 0 and 1.

## foundation/how-computers-work/how-letters-become-numbers
interview-questions:
- [intern] How does a computer represent a text character like 'A' using numbers?
- [junior] What is the difference between ASCII and UTF-8 encoding, and why does UTF-8 use variable-length bytes?
- [mid] Why can reading a file or network stream assuming fixed-width characters corrupt multi-byte UTF-8 characters like emojis or non-English text?
- [senior] What goes wrong in production when substring index operations are performed on strings containing multi-byte UTF-8 characters or surrogate pairs?
what-interviewers-listen-for:
- Character encodings map numbers to glyphs. ASCII uses 7/8 bits for English; UTF-8 uses 1 to 4 bytes per character for universal Unicode support.
- UTF-8 is backward-compatible with ASCII because standard English characters occupy exactly 1 byte.
- Indexing strings by raw byte offset or character code unit index instead of grapheme clusters breaks multi-byte characters and surrogate pairs.
build-exercise: Create a Kotlin string containing an ASCII character, a accented character, and an emoji. Print its length, its UTF-8 byte array size, and iterate through its `indices` vs `codePoints` to observe how code units differ from character counts.
official-references:
- Strings | https://kotlinlang.org/docs/strings.html
common-wrong-beliefs:
- Every character in a string always occupies exactly one byte or one index position | In UTF-8, characters can take 1 to 4 bytes, and some visual characters consist of multiple code points (grapheme clusters).

## foundation/how-computers-work/what-is-a-cpu
interview-questions:
- [intern] What is a CPU core, and what is the difference between a single-core and a multi-core processor?
- [junior] What is clock speed (GHz) and why is a higher clock speed not the sole determinant of execution speed?
- [mid] How do modern mobile CPUs use heterogeneous core architectures (e.g., efficiency cores vs performance cores)?
- [senior] What goes wrong in production when heavy background synchronization or polling tasks wake up performance cores constantly, draining battery?
what-interviewers-listen-for:
- CPUs fetch, decode, and execute instructions from memory through an instruction pipeline.
- Mobile processors (ARM big.LITTLE / DynamIQ) pair high-performance cores for heavy computation with energy-efficient cores for idle/background work.
- Waking performance cores unnecessarily causes thermal throttling and steep battery drain.
build-exercise: Check Android's `Runtime.getRuntime().availableProcessors()` in code to see how many cores the device reports, and inspect how work allocation differs across cores.
official-references:
- Processes and threads overview | https://developer.android.com/guide/components/processes-and-threads
common-wrong-beliefs:
- A 3GHz processor always runs three times faster than a 1GHz processor | Architecture efficiency (instructions per cycle), cache size, and memory latency dictate actual execution speed.

## foundation/how-computers-work/memory-vs-storage
interview-questions:
- [intern] What is the fundamental difference between volatile memory (RAM) and non-volatile storage (Flash / SSD)?
- [junior] Why is reading from storage orders of magnitude slower than reading from RAM?
- [mid] How does virtual memory paging allow an OS to run apps whose memory footprint exceeds physical RAM?
- [senior] What goes wrong in production when an app performs frequent synchronous file I/O or SQLite queries on the main thread because storage is treated like memory?
what-interviewers-listen-for:
- RAM is volatile, byte-addressable, and fast; storage (Flash/UFS) is persistent, block-addressable, and significantly slower.
- Flash storage suffers from write wear and block-erase latency overheads.
- Treating storage I/O as instantaneous blocks the main thread and causes UI jank or ANRs.
build-exercise: Measure the execution time of writing 1MB of text to internal storage (disk) versus writing to a local ByteArray in RAM. Log the duration difference to observe the cost of disk I/O.
official-references:
- Storage options | https://developer.android.com/training/data-storage
common-wrong-beliefs:
- Flash storage on a modern phone is just as fast as RAM | RAM operates on nanosecond memory bus speeds, whereas flash storage operations take microseconds to milliseconds.

## foundation/how-computers-work/what-is-an-os
interview-questions:
- [intern] What is an Operating System, and what is its primary role between hardware and applications?
- [junior] What is the difference between user space and kernel space in an operating system?
- [mid] How does the Linux kernel manage process isolation and memory protection on Android?
- [senior] What goes wrong in production when a native C++ library attempts illegal memory access, causing a SIGSEGV segmentation fault?
what-interviewers-listen-for:
- The OS abstracts hardware, manages resource scheduling, enforces security, and isolates processes.
- User space apps cannot access hardware or kernel memory directly; they must make system calls crossing the user/kernel boundary.
- Process isolation ensures one crashing app cannot corrupt the memory space of another app or the kernel.
build-exercise: Trigger an intentional exception (such as division by zero in Java/Kotlin vs native crash) and inspect Logcat to see how the OS intercepts the fault and logs the stack trace or tombstone.
official-references:
- Android Architecture | https://developer.android.com/guide/platform
common-wrong-beliefs:
- Apps have direct access to physical hardware components like the camera sensor or radio chip | Apps must request access through OS system services and drivers managed by the kernel.

## foundation/first-code/what-is-code
interview-questions:
- [intern] What is programming code, and why must instructions be written with absolute syntactic precision?
- [junior] What is the difference between human natural language and computer programming languages regarding ambiguity?
- [mid] How do programming abstractions (like functions and classes) translate human intent into machine execution steps?
- [senior] What goes wrong in production when ambiguous business requirements are translated into rigid conditional logic without edge-case handling?
what-interviewers-listen-for:
- Code is a formal set of unambiguous instructions executed step-by-step by a processor.
- Natural language tolerates context and ambiguity; compilers reject syntax errors and ambiguous control flows completely.
build-exercise: Write a simple Kotlin function with a deliberate syntax typo, observe the compiler error message, and fix it to understand how strict compilation checks are.
official-references:
- Kotlin basic syntax | https://kotlinlang.org/docs/basic-syntax.html
common-wrong-beliefs:
- A programming language "understands" what you mean even if there is a typo | Compilers parse syntax strictly according to grammar rules; a single missing character prevents code generation entirely.

## foundation/first-code/run-your-first-program
interview-questions:
- [intern] Where can you write and execute Kotlin code without installing Android Studio or setting up a project?
- [junior] What is the purpose of an online playground or REPL (Read-Eval-Print Loop) in learning a language?
- [mid] How does an interactive REPL differ in execution model from compiling a full standalone application binary?
- [senior] What goes wrong in production when code tested only in a quick REPL snippet encounters threading, context, or lifecycle constraints in a real app?
what-interviewers-listen-for:
- Playgrounds and REPLs evaluate expressions immediately, aiding rapid experimentation.
- REPL environments abstract build systems and manifest configurations, hiding environment-specific constraints.
build-exercise: Open Kotlin Playground (play.kotlinlang.org), write a short script printing a greeting and filtering a list, and run it instantly in the browser.
official-references:
- Kotlin Playground | https://play.kotlinlang.org/
common-wrong-beliefs:
- Code that runs successfully in a standalone Kotlin Playground script is guaranteed to work inside an Android app | Android apps require framework initialization, context, lifecycle management, and thread constraints not present in plain Kotlin scripts.

## foundation/first-code/lines-run-in-order
interview-questions:
- [intern] In what order does a computer execute lines of code in a standard script or function?
- [junior] How do control flow statements (if, loops, function calls) alter the sequential execution path?
- [mid] How does synchronous execution differ from asynchronous or event-driven execution order?
- [senior] What goes wrong in production when assumptions about line-by-line execution order are violated by asynchronous callbacks or concurrency?
what-interviewers-listen-for:
- Code executes top-to-bottom sequentially unless branch or jump instructions alter the instruction pointer.
- Asynchronous callbacks and coroutine suspension points mean lines written below a call may execute before lines inside an asynchronous operation finish.
build-exercise: Print three numbered log statements in sequence (`1`, `2`, `3`), then insert a `delay` or asynchronous block in the middle and observe how the print order changes.
official-references:
- Control flow | https://kotlinlang.org/docs/control-flow.html
common-wrong-beliefs:
- A function call placed on line 10 always finishes computing its result before line 11 executes | If the function is asynchronous or launches a coroutine, execution flows immediately to line 11 before the background task completes.

## foundation/programming-thinking/what-is-a-variable
interview-questions:
- [intern] What is a variable in programming, and what three attributes does every variable have?
- [junior] What is the difference between a variable holding a primitive value versus a reference to an object in memory?
- [mid] How does stack memory allocation differ from heap memory allocation for variables?
- [senior] What goes wrong in production when mutable reference variables are shared across multiple threads without synchronization?
what-interviewers-listen-for:
- A variable is a named storage location associated with a value, a type, and a memory address.
- Primitives store actual values directly on the stack; reference variables store a memory address pointing to an object allocated on the heap.
- Shared mutable references across threads cause data races and state corruption.
build-exercise: Declare a primitive integer variable and an object reference variable, assign them, reassign their values, and observe how variable references behave.
official-references:
- Properties | https://kotlinlang.org/docs/properties.html
common-wrong-beliefs:
- Assigning one object variable to another creates a brand-new duplicate copy of the object | In object-oriented languages, assignment copies the object reference address, so both variables point to the exact same heap object.

## foundation/programming-thinking/what-is-type
interview-questions:
- [intern] What is a data type, and why does a computer need to know whether a memory location holds an integer, a decimal, or text?
- [junior] What is the difference between statically-typed languages (like Kotlin) and dynamically-typed languages (like Python/JavaScript)?
- [mid] How does compile-time type checking prevent entire classes of runtime errors compared to dynamic typing?
- [senior] What goes wrong in production when unchecked type casts (`as`) or platform types bypass type safety boundaries?
what-interviewers-listen-for:
- Types define the domain of values and valid operations permitted on a variable.
- Static typing catches type mismatches at compile time before the app ever ships to users.
- Forced unchecked casts (`as`) risk `ClassCastException` at runtime when API payloads or local state mismatch expectations.
build-exercise: Attempt to assign a String to an Int variable in Kotlin and observe the compiler error; then try an unsafe type cast (`as Int`) on a String object and observe the runtime exception.
official-references:
- Types | https://kotlinlang.org/docs/types.html
common-wrong-beliefs:
- Data types are just labels for human readability and do not affect machine execution | CPU instructions differ entirely depending on whether bytes represent integer arithmetic, floating-point math, or memory addresses.

## foundation/programming-thinking/expressions
interview-questions:
- [intern] What is an expression in programming, and how does it differ from a statement?
- [junior] How do operator precedence and associativity determine how an expression like `3 + 4 * 2` is evaluated?
- [mid] Why does Kotlin treating `if` and `when` as expressions rather than statements improve code readability and immutability?
- [senior] What goes wrong in production when complex expressions with side effects are evaluated in guard clauses or boolean short-circuiting?
what-interviewers-listen-for:
- An expression evaluates to a value; a statement performs an action or side effect.
- Operator precedence ensures deterministic evaluation order (e.g., multiplication before addition).
- Expression-based control flow allows assigning `val` directly without temporary mutable `var` variables.
build-exercise: Write an `if` expression that returns a String based on a condition and assign it directly to a `val`, contrasting it with a traditional `var` assigned inside an `if` statement block.
official-references:
- Control flow | https://kotlinlang.org/docs/control-flow.html
common-wrong-beliefs:
- An `if` statement in Kotlin is just syntax sugar for a ternary operator (`? :`) | Kotlin uses expression-based `if` and `when` blocks where any branch can contain multiple lines, returning its last expression value.

## foundation/programming-thinking/yes-or-no-questions
interview-questions:
- [intern] How does a computer evaluate a boolean (true/false) expression?
- [junior] What are comparison operators (`==`, `!=`, `<`, `>`, `<=`, `>=`) and logical operators (`&&`, `||`, `!`)?
- [mid] What is short-circuit evaluation in logical expressions, and why is it important for preventing null pointer exceptions?
- [senior] What goes wrong in production when complex boolean expressions lack proper parentheses, leading to operator precedence bugs?
what-interviewers-listen-for:
- Booleans represent binary truth values. Logical operators combine them.
- Short-circuiting (`&&` and `||`) stops evaluating the right-hand side as soon as the final result is determined.
- Safe navigation (`?.`) and short-circuiting (`!= null && obj.isValid()`) prevent crashes by avoiding evaluation when the left side is false or null.
build-exercise: Evaluate a compound boolean expression combining `&&` and `||` with a null check on the left side, and verify that short-circuiting prevents evaluating the right side when null.
official-references:
- Basic types | https://kotlinlang.org/docs/basic-types.html
common-wrong-beliefs:
- Both sides of an `&&` expression are always evaluated regardless of the left side's value | In short-circuit evaluation, if the left side is false, the right side is skipped entirely.

## foundation/programming-thinking/how-a-program-decides
interview-questions:
- [intern] How do `if`, `else if`, and `else` control structures allow a program to make decisions?
- [junior] When is a `when` expression preferred over a multi-branched `if-else` ladder in Kotlin?
- [mid] How does the compiler optimize `when` expressions with exhaustive branch matching (especially with sealed classes/enums)?
- [senior] What goes wrong in production when an unhandled enum or sealed class branch is added later without updating legacy `when` logic?
what-interviewers-listen-for:
- Branching logic directs execution down alternate paths based on runtime boolean conditions.
- Kotlin `when` acts as a powerful pattern matcher and replacement for switch-case statements.
- Exhaustive `when` statements enforced by the compiler prevent missing new state cases when sealed classes expand.
build-exercise: Write a `when` expression over an enum with three values, compile it, then add a fourth enum value and observe the compiler warning/error when the `when` is non-exhaustive.
official-references:
- Control flow | https://kotlinlang.org/docs/control-flow.html
common-wrong-beliefs:
- An `else` branch is always required in a Kotlin `when` expression | If the subject is a sealed class or enum whose branches cover all possible values exhaustively, an `else` branch is optional.

## foundation/programming-thinking/how-a-program-repeats
interview-questions:
- [intern] What is a loop, and why are loops essential for automating repetitive tasks in programming?
- [junior] What is the difference between a `while` loop and a `do-while` loop regarding condition checking?
- [mid] When should you choose a `while` loop over a bounded `for` loop in mobile application development?
- [senior] What goes wrong in production when an infinite `while` loop lacks a proper termination condition or cooperative cancellation check?
what-interviewers-listen-for:
- Loops execute a block of code repeatedly while a condition evaluates to true.
- `while` checks the condition before the first iteration; `do-while` executes the block at least once before checking the condition.
- Infinite loops without suspension or thread yielding consume 100% CPU on that core, causing device thermal throttling and ANRs.
build-exercise: Write a `while` loop that increments a counter up to 5 and terminates, then compare it with a `do-while` loop execution order.
official-references:
- Control flow | https://kotlinlang.org/docs/control-flow.html
common-wrong-beliefs:
- A `do-while` loop runs at least twice | A `do-while` loop executes its body once, then evaluates the condition to decide whether to run a second time.

## foundation/programming-thinking/counting-loops
interview-questions:
- [intern] How do you write a counted `for` loop in Kotlin to repeat an operation a specific number of times?
- [junior] What is the difference between `..`, `until`, and `step` when constructing ranges in Kotlin loops?
- [mid] Why does Kotlin prefer iterator-based `for (item in collection)` loops over C-style index loops (`for (i = 0; i < size; i++)`)?
- [senior] What goes wrong in production when iterating over a mutable collection with index assumptions while another thread modifies its size?
what-interviewers-listen-for:
- Kotlin ranges (`1..10` inclusive, `1 until 10` exclusive) provide clean bounds syntax for loops.
- Idiomatic Kotlin iterates over elements or items directly rather than manual index arithmetic, reducing out-of-bounds errors.
- Modifying a collection while iterating over it without a thread-safe iterator or snapshot throws a `ConcurrentModificationException`.
build-exercise: Loop through numbers from 1 to 10 with a step of 2 using `indices` and `step`, and print each value.
official-references:
- Ranges and progressions | https://kotlinlang.org/docs/ranges.html
common-wrong-beliefs:
- `1..10` in a Kotlin loop includes the number 10 | `..` is inclusive of both bounds; use `1 until 10` to exclude the upper bound.

## foundation/programming-thinking/what-is-a-list
interview-questions:
- [intern] What is a list collection, and how does it differ from holding individual separate variables?
- [junior] What is the difference between a read-only `List` and a mutable `MutableList` in Kotlin?
- [mid] How does an array-backed list (`ArrayList`) manage internal memory allocation and resizing when items are added?
- [senior] What goes wrong in production when large lists are repeatedly copied or filtered without backing capacity tuning or lazy sequences?
what-interviewers-listen-for:
- Lists store ordered sequences of elements accessible by numeric index.
- Kotlin's read-only `List` interface enforces read immutability at the API boundary, though the underlying implementation may still be mutable.
- ArrayList resizing allocates a new larger backing array (typically 1.5x capacity) and copies existing references, which can cause GC pressure if done frequently in tight loops.
build-exercise: Create a `MutableList`, add five items, read by index, and attempt to cast or assign it to a read-only `List` to observe interface segregation.
official-references:
- Collections overview | https://kotlinlang.org/docs/collections-overview.html
common-wrong-beliefs:
- A Kotlin `List` is guaranteed to be immutable and unmodifiable | Kotlin's `List` interface is read-only; if the underlying implementation is an `ArrayList`, code with a reference to the `MutableList` can still modify it.

## foundation/programming-thinking/loop-over-a-list
interview-questions:
- [intern] How do you iterate over every item in a list to process each element in Kotlin?
- [junior] How do you access both the index and the value of items simultaneously using `withIndex()` or `forEachIndexed`?
- [mid] When should you use a standard `for` loop versus functional higher-order functions like `forEach` or `map` on a list?
- [senior] What goes wrong in production when performing expensive operations or UI mutations inside a synchronous list loop on the main thread?
what-interviewers-listen-for:
- Iteration processes each element sequentially.
- `for (item in list)` is readable and supports `break`/`continue`, whereas `forEach` lambda scopes prevent local returns without labeled breaks.
- Processing large lists synchronously on the main thread blocks UI responsiveness.
build-exercise: Create a list of strings, loop through them using `for (item in list)` and `forEachIndexed`, and print formatted output for each element.
official-references:
- Collections overview | https://kotlinlang.org/docs/collections-overview.html
common-wrong-beliefs:
- You can use `break` and `continue` keywords inside a lambda-based `forEach` loop | `forEach` takes a lambda; `break` and `continue` are invalid inside lambdas unless using labeled returns (`return@forEach`).

## foundation/programming-thinking/what-is-a-function
interview-questions:
- [intern] What is a function, and why is modularizing code into functions fundamental to software engineering?
- [junior] What are function inputs (parameters), function signature, and return values?
- [mid] What is the principle of single responsibility for functions, and why are large monolithic functions hard to test?
- [senior] What goes wrong in production when functions maintain hidden side effects (mutating global state or singleton variables)?
what-interviewers-listen-for:
- A function is a reusable encapsulated block of code designed to perform a specific task given inputs and return an output.
- Pure functions (no side effects, deterministic output for the same input) are easiest to test and reason about.
- Hidden side effects cause unpredictable bugs when functions are called in different orders or concurrently.
build-exercise: Write a pure function that calculates the sum of two numbers, and a non-pure function that modifies an external global variable. Test both to observe why pure functions are predictable.
official-references:
- Functions | https://kotlinlang.org/docs/functions.html
common-wrong-beliefs:
- A function must always take inputs and return a value | Functions can take zero inputs (parameterless) and return `Unit` (nothing of interest), acting purely for side effects.

## foundation/programming-thinking/function-inputs-and-outputs
interview-questions:
- [intern] How do you pass data into a function and receive a computed result back?
- [junior] What is the difference between pass-by-value and pass-by-reference when passing arguments into functions?
- [mid] How do named arguments and default parameter values in Kotlin eliminate the need for method overloading?
- [senior] What goes wrong in production when default parameters reference mutable objects or evaluate expressions at declaration time instead of call time?
what-interviewers-listen-for:
- Parameters receive copies of primitive values or copies of object reference addresses.
- Named arguments improve readability and prevent parameter-ordering bugs when functions have multiple parameters of the same type.
- Default parameters evaluated at declaration time retain stale references if they instantiate mutable objects.
build-exercise: Write a function with default arguments and call it using named argument syntax out of order to verify parameter flexibility.
official-references:
- Functions | https://kotlinlang.org/docs/functions.html
common-wrong-beliefs:
- Kotlin passes objects to functions by pass-by-reference | Kotlin (like Java) uses pass-by-value for references; the reference address value is copied, allowing method calls to mutate internal object state but not reassign the caller's variable reference.

## foundation/programming-thinking/trace-code-by-hand
interview-questions:
- [intern] What does it mean to "trace code by hand" (desk checking), and why is it a vital debugging skill?
- [junior] How do you use a mental execution model (tracking variables, stack frames, and loop states) on paper?
- [mid] Why is hand-tracing especially effective for identifying off-by-one errors in loops and edge cases in conditional branches?
- [senior] What goes wrong in production when developers rely solely on running code and guessing instead of systematically tracing complex algorithmic logic?
what-interviewers-listen-for:
- Step-by-step mental simulation builds deep comprehension of state changes over time.
- Tracing prevents wasted hours guessing bug fixes by pinpointing exact variable divergence points.
build-exercise: Take a 10-line snippet with a loop and an accumulator variable, trace its values on paper for each iteration, and verify your prediction by running it.
official-references:
- Debugging apps | https://developer.android.com/studio/debug
common-wrong-beliefs:
- Code tracing is only for beginners who don't know how to use a debugger | Experienced engineers use hand-tracing during code reviews and architectural design to verify correctness before writing code.

## foundation/debugging-mindset/reading-error-messages
interview-questions:
- [intern] What are the key parts of a stack trace when an app crashes in Android Studio?
- [junior] How do you read from the bottom to the top of a stack trace to find the root cause of an exception?
- [mid] What is the difference between a compile-time error, a lint warning, a runtime exception, and a fatal native crash?
- [senior] What goes wrong in production when obfuscated release builds (ProGuard/R8) lack proper mapping files to de-obfuscate crash stack traces?
what-interviewers-listen-for:
- Stack traces show the exact call stack leading to an exception; the root cause is typically at the bottom of the chain or indicated by `Caused by:`.
- Compile-time errors block builds; runtime exceptions crash threads or apps; obfuscated stack traces require mapping files (`mapping.txt`) to translate hashed method names back to readable symbols.
build-exercise: Intentionally trigger a `NullPointerException` or `IndexOutOfBoundsException`, read the stack trace in Logcat, locate the exact file and line number, and fix it.
official-references:
- Debug your app | https://developer.android.com/studio/debug
common-wrong-beliefs:
- The top line of a stack trace always tells you where the bug in your code is located | The top line shows where the exception was thrown (often framework or library code); you must look down the stack to find your app's code that triggered it.

## foundation/debugging-mindset/what-is-a-bug
interview-questions:
- [intern] What is a software bug, and why do programs run without compiler errors yet still produce incorrect behavior?
- [junior] What is the difference between a syntax error, a logic bug, and a regression?
- [mid] How do edge cases (empty inputs, extreme values, concurrent modifications) give rise to production bugs?
- [senior] What goes wrong in production when bugs manifest non-deterministically due to race conditions or intermittent network failures?
what-interviewers-listen-for:
- Bugs are discrepancies between expected requirements and actual code execution logic.
- Logic bugs compile successfully because syntax is valid, but operational assumptions are flawed.
- Race conditions and non-deterministic concurrency bugs are notoriously difficult to reproduce without automated stress tests.
build-exercise: Write a function with an off-by-one error in a boundary check that compiles successfully, write a test case exposing the bug, and fix it.
official-references:
- Test your app | https://developer.android.com/training/testing
common-wrong-beliefs:
- If code compiles without warnings or errors, it is logically correct | Compilation only proves syntactic and type correctness; it guarantees nothing about business logic or functional correctness.

## foundation/debugging-mindset/find-a-bug-without-guessing
interview-questions:
- [intern] What is the scientific method of debugging (formulate hypothesis, test, observe) versus random guessing?
- [junior] How do you use breakpoints, variable inspection, and conditional logging to isolate the exact moment state becomes incorrect?
- [mid] What is a binary search debugging strategy (git bisect) for finding which commit introduced a regression?
- [senior] What goes wrong in production when developers fix symptoms of a bug rather than investigating root architectural causes?
what-interviewers-listen-for:
- Systematic debugging isolates variables and reproduces the issue reliably before applying a targeted fix.
- `git bisect` binary-searches commit history to pinpoint the exact commit causing a regression in minutes.
- Fixing symptoms without understanding root causes leads to recurring bugs and fragile codebases.
build-exercise: Use `git bisect` in a practice repository with a regression commit, or set a conditional breakpoint in Android Studio that pauses execution only when a variable equals an invalid value.
official-references:
- Debug with Android Studio | https://developer.android.com/studio/debug
common-wrong-beliefs:
- Changing random lines of code until the error goes away is an effective debugging strategy | Random code changes often introduce new bugs or mask underlying issues without solving the root cause.

## foundation/debugging-mindset/check-code-you-did-not-write
interview-questions:
- [intern] What precautions should you take when incorporating third-party code snippets or AI-generated code into your app?
- [junior] How do you audit unfamiliar code for security vulnerabilities, memory leaks, and performance bottlenecks?
- [mid] Why do copy-pasted solutions often fail when integrated into a different architecture or Kotlin version?
- [senior] What goes wrong in production when un-reviewed third-party dependencies or AI snippets introduce hidden background threads, memory leaks, or data exfiltration?
what-interviewers-listen-for:
- Unverified third-party code may contain outdated APIs, memory leaks, thread violations, or security backdoors.
- Code review requires checking lifecycle bounds, null safety, and error handling.
build-exercise: Take an AI-generated snippet or old StackOverflow Java solution, review it for modern Kotlin idiom compliance, null safety, and coroutine lifecycle safety, and refactor it.
official-references:
- Android security best practices | https://developer.android.com/topic/security/best-practices
common-wrong-beliefs:
- If code found online or generated by AI compiles successfully, it is production-ready | Automated snippets frequently omit lifecycle management, error handling, thread safety, and edge-case validation.

## foundation/from-code-to-running/what-is-a-compiler
interview-questions:
- [intern] What is a compiler, and what is its role in translating human-readable source code into machine instructions?
- [junior] What are the stages of compilation (lexical analysis, parsing, type checking, code generation, optimization)?
- [mid] How does incremental compilation in Gradle speed up builds by only recompiling modified files?
- [senior] What goes wrong in production when compiler optimization flags strip or reorder code in ways that break reflection or native JNI bindings?
what-interviewers-listen-for:
- Compilers translate high-level source code into intermediate bytecode or machine code.
- Optimization passes improve execution speed and reduce binary size.
- Aggressive code shrinking/obfuscation can break reflection or native method signatures unless ProGuard/R8 keep rules are defined.
build-exercise: Inspect the build output directory of an Android project to see generated intermediate files and class outputs after running a Gradle build.
official-references:
- Configure your build | https://developer.android.com/build
common-wrong-beliefs:
- The compiler runs every single line of your code when you click build | The compiler translates source code into executable binary format; execution happens later when the compiled binary runs on a device.

## foundation/from-code-to-running/compiler-vs-interpreter
interview-questions:
- [intern] What is the fundamental difference between a compiled language (like Kotlin/C++) and an interpreted language (like standard Python/JavaScript)?
- [junior] What is JIT (Just-In-Time) compilation vs AOT (Ahead-Of-Time) compilation?
- [mid] Why does modern Android use a hybrid approach combining AOT compilation and JIT profiling (via ART)?
- [senior] What goes wrong in production when JIT-compiled hot methods lack Baseline Profiles during initial app startup, causing jank?
what-interviewers-listen-for:
- Compiled languages translate code ahead of execution; interpreted languages execute via an interpreter line-by-line (though modern runtimes use JIT).
- ART compiles DEX bytecode to machine code AOT and during installation/bg compilation, while JIT profiling optimizes hot methods at runtime.
- Baseline Profiles supply pre-compiled machine code instructions for critical startup paths, eliminating initial JIT compilation overhead.
build-exercise: Inspect an app's build outputs to find generated DEX files and understand how Kotlin compiles into DEX bytecode for Android runtime.
official-references:
- Android Runtime (ART) and Dalvik | https://source.android.com/devices/tech/dalvik
common-wrong-beliefs:
- Kotlin code runs directly as native machine code on an Android phone without any intermediate runtime translation | Kotlin compiles to JVM-compatible bytecode packaged as DEX, which is executed and further compiled to native machine code by ART on the device.

## foundation/from-code-to-running/what-is-the-jvm
interview-questions:
- [intern] What is the Java Virtual Machine (JVM), and what problem did it solve ("write once, run anywhere")?
- [junior] What is the role of JVM bytecode as an intermediate representation between source code and machine hardware?
- [mid] How does the JVM garbage collector manage heap memory allocation and reclamation automatically?
- [senior] What goes wrong in production when garbage collection pauses (Stop-The-World) cause frame drops or UI jank during animations?
what-interviewers-listen-for:
- The JVM executes compiled platform-independent bytecode on top of host OS hardware.
- Garbage collection reclaims heap memory from unreferenced objects, but frequent allocations trigger GC cycles that can cause frame drops.
- Android uses ART (Android Runtime) rather than a traditional desktop JVM, optimized for mobile memory and battery constraints.
build-exercise: Write a loop that allocates thousands of temporary objects in memory, run it while watching Memory Profiler in Android Studio, and observe garbage collection allocation spikes.
official-references:
- Android Runtime (ART) and Dalvik | https://source.android.com/devices/tech/dalvik
common-wrong-beliefs:
- Android apps run directly on a standard desktop JVM | Android apps execute DEX bytecode via ART (Android Runtime), which evolved from Dalvik, sharing Java language semantics but utilizing a different runtime and bytecode format.

## foundation/from-code-to-running/what-is-art
interview-questions:
- [intern] What is ART (Android Runtime), and what is its role on an Android device?
- [junior] How did ART replace the older Dalvik virtual machine, shifting from pure JIT to AOT/JIT hybrid compilation?
- [mid] What is Profile-Guided Compilation (PGO) in ART, and how do Baseline Profiles leverage it?
- [senior] What goes wrong in production when an app lacks Baseline Profiles, forcing ART to interpret and JIT-compile bytecode during cold startup?
what-interviewers-listen-for:
- ART executes app DEX bytecode and compiles it to native machine instructions.
- Baseline Profiles provide ART with hot-method execution paths ahead of time, ensuring smooth first launches without JIT compilation delays.
- Without Baseline Profiles, cold starts suffer from higher CPU usage and slower frame rendering.
build-exercise: Generate and inspect a Baseline Profile configuration in an Android project using Android Gradle Plugin tooling.
official-references:
- All about Baseline Profiles | https://developer.android.com/topic/performance/baseline-profiles
common-wrong-beliefs:
- Android devices interpret Kotlin source files directly at runtime | Android devices execute pre-compiled DEX bytecode packaged inside the app APK/AAB via ART.

## foundation/how-an-app-is-born/what-is-an-app-file
interview-questions:
- [intern] What file extension do Android apps use when installed on a phone (APK) versus uploaded to Google Play (AAB)?
- [junior] What is inside an APK file archive (classes.dex, AndroidManifest.xml, resources.arsc, res/, lib/)?
- [mid] How does Google Play use Android App Bundles (AABs) to generate optimized, device-specific split APKs for users?
- [senior] What goes wrong in production when native library ABIs (armeabi-v7a, arm64-v8a, x86_64) are incorrectly bundled or missing in the final APK?
what-interviewers-listen-for:
- An APK is a ZIP archive containing compiled DEX code, binary XML manifests, compiled resources, and native libraries.
- AABs allow Play Store to deliver only the code, language, and screen density assets needed for the user's specific device, reducing download size.
- Missing native library ABIs crash the app with `UnsatisfiedLinkError` on unsupported hardware architectures.
build-exercise: Build an APK and open it in Android Studio's APK Analyzer tool to inspect file sizes, classes.dex contents, and asset packaging.
official-references:
- Analyze your build with APK Analyzer | https://developer.android.com/studio/debug/apk-analyzer
common-wrong-beliefs:
- You upload an APK directly to Google Play for users to download | Modern Google Play distribution requires an Android App Bundle (AAB), from which Google Play generates optimized APKs for each specific user device.

## foundation/how-an-app-is-born/what-happens-when-i-build
interview-questions:
- [intern] What happens when you press the "Sync Project with Gradle Files" and "Build" buttons in Android Studio?
- [junior] How do the Android Gradle Plugin (AGP) and Kotlin compiler work together during a compilation build pass?
- [mid] What is the role of R8 / ProGuard during a release build (code shrinking, obfuscation, optimization)?
- [senior] What goes wrong in production when R8 aggressive shrinking strips classes or methods used via reflection or serialization libraries?
what-interviewers-listen-for:
- Building compiles Kotlin/Java source code to JVM bytecode, converts it to DEX bytecode via D8/R8, compiles XML resources, and packages the APK/AAB archive.
- R8 shrinks unused code, obfuscates symbol names, and optimizes bytecode for release builds.
- Missing ProGuard/R8 keep rules (`@Keep` or consumer proguard files) cause runtime `ClassNotFoundException` or `NoSuchMethodException` when reflection or JSON serialization is used.
build-exercise: Run a release build with R8 code shrinking enabled, and inspect the resulting mapping.txt file and APK size reduction in APK Analyzer.
official-references:
- Shrink, obfuscate, and optimize your app | https://developer.android.com/build/shrink-code
common-wrong-beliefs:
- Enabling code shrinking and obfuscation (R8) guarantees your API keys and code strings are completely secret | R8 obfuscates names and removes unused code, but strings and constants remain inspectable via reverse engineering tools.

## foundation/how-an-app-is-born/how-an-app-gets-installed
interview-questions:
- [intern] How does an app transition from a downloaded file on your phone into an installed application?
- [junior] What is the role of digital app signatures (signing certificates) in Android installation and updates?
- [mid] Why does Android enforce signature matching when an app is updated over an existing version?
- [senior] What goes wrong in production when signing keys are lost or compromised, or when debug keys leak into release builds?
what-interviewers-listen-for:
- PackageInstaller validates app signatures, verifies manifest permissions, extracts DEX/native libraries, and assigns a unique Linux UID to the app sandbox.
- App signatures guarantee code provenance and prevent malicious apps from hijacking updates of existing packages.
- Losing release signing keys permanently prevents publishing updates to existing app package names on Google Play.
build-exercise: Inspect the signing configuration in your app's `build.gradle.kts` file and verify how debug versus release keystores are configured.
official-references:
- Sign your app | https://developer.android.com/build/building-cmdline#sign_app
common-wrong-beliefs:
- Anyone can publish an update to your app on Google Play as long as they know your app's package name (e.g., com.example.app) | Google Play and the Android OS require matching cryptographic signing keys to authorize app updates, preventing impersonation.

## foundation/how-an-app-is-born/what-happens-when-i-tap-an-icon
interview-questions:
- [intern] What happens, step by step, from the moment you tap an app icon on your home screen to seeing the first screen?
- [junior] What is the role of the Launcher (homescreen app) and ActivityManager / ActivityTaskManager in starting a new app process?
- [mid] What is the difference between cold, warm, and hot app starts regarding process creation and memory initialization?
- [senior] What goes wrong in production when heavy ContentProviders or Application.onCreate logic delays the first frame display during a cold start?
what-interviewers-listen-for:
- Tapping an icon sends an Intent to the system Launcher, which calls `startActivity`. The OS forks a new process from Zygote if not running, binds the Application, and schedules the first Activity lifecycle traversal.
- Cold start initializes everything from scratch (process fork, Application.onCreate, Activity creation, layout inflation, first frame draw); warm and hot starts reuse existing process state.
- Heavy initialization in Application.onCreate or custom ContentProviders blocks cold start performance and metrics (Time to Initial Display).
build-exercise: Measure app startup time using ADB commands (`am start -W`) and inspect Logcat startup timing metrics (`Displayed` time).
official-references:
- Understand app startup time | https://developer.android.com/topic/performance/vitals/launch-time
common-wrong-beliefs:
- Tapping an app icon immediately opens the Activity UI code without involving the operating system kernel or process creation | Tapping an icon fires a system intent handled by the OS process manager, which must fork a Linux process and initialize the Android runtime before any app code runs.

## foundation/how-an-app-is-born/run-your-first-app
interview-questions:
- [intern] How do you connect an emulator or a physical Android device to Android Studio to run your app?
- [junior] What is USB Debugging, and how do developer options enable communication between your computer and phone via ADB?
- [mid] Why might an emulator fail to start or run slowly (e.g., missing hardware virtualization extensions like Intel HAXM or AMD KVM)?
- [senior] What goes wrong in production when testing only on high-end developer emulators without verifying performance on low-end physical hardware?
what-interviewers-listen-for:
- Android Debug Bridge (ADB) bridges the host development machine to the target device or emulator over USB or TCP/IP.
- Hardware virtualization (VT-x/AMD-V) is essential for fast emulator execution.
- Testing exclusively on high-end emulators masks low-end memory limits, slow CPU throttling, and UI jank experienced by real users.
build-exercise: Enable Developer Options and USB Debugging on a physical device, connect it via USB, and run your first "Hello World" app from Android Studio onto the device.
official-references:
- Run apps on a hardware device | https://developer.android.com/studio/run/device
common-wrong-beliefs:
- An Android emulator is just a video player showing a pre-recorded phone screen | An emulator is a full system virtualization environment running an emulated CPU architecture and Android OS image on your computer.

## Self-check
1. Android performance overview | https://developer.android.com/topic/performance
2. Basic types | https://kotlinlang.org/docs/basic-types.html
3. Strings | https://kotlinlang.org/docs/strings.html
4. Processes and threads overview | https://developer.android.com/guide/components/processes-and-threads
5. Storage options | https://developer.android.com/training/data-storage
6. Android Architecture | https://developer.android.com/guide/platform
7. Kotlin basic syntax | https://kotlinlang.org/docs/basic-syntax.html
8. Kotlin Playground | https://play.kotlinlang.org/
9. Control flow | https://kotlinlang.org/docs/control-flow.html
10. Properties | https://kotlinlang.org/docs/properties.html