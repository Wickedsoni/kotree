package dev.kotree.site.tree.model

import dev.kotree.site.Tokens

/** Hard-coded sample data: boughs and titles exactly docs/03 §1 and docs/00 §5. No stems, no leaves. */
val sampleTree: TreeData = TreeData(
    schema = 1,
    seed = Tokens.World.seed,
    boughs = listOf(
        bough("roots", "Foundation", 0, branch("foundation", "Foundation", 0)),
        bough(
            "kotlin-android", "Kotlin and Android", 1,
            branch("kotlin", "Kotlin language", 0),
            branch("android-core", "Android core and lifecycles", 1),
            branch("ui", "UI (Compose and Views)", 2),
            branch("concurrency", "Coroutines, Flow, threads", 3),
        ),
        bough(
            "architecture-kmp", "Architecture and KMP", 2,
            branch("architecture-data", "Architecture and data", 0),
            branch("app-networking", "Networking inside apps", 1),
            branch("kmp", "Kotlin Multiplatform", 2),
        ),
        bough(
            "cs-theory", "CS theory", 3,
            branch("dsa", "Data structures and algorithms", 0),
            branch("daa", "Design and analysis of algorithms", 1),
            branch("os", "Operating systems", 2),
            branch("networking", "Computer networking", 3),
        ),
        bough(
            "craft", "Craft", 4,
            branch("testing", "Testing", 0),
            branch("profiling", "Profiling and optimisation", 1),
            branch("native-ndk", "Native layer (NDK, JNI, ART)", 2),
            branch("security", "Security", 3),
            branch("tools", "Tools and ecosystem", 4),
        ),
    ),
    counts = Counts(live = 0, total = 0),
)

private fun bough(id: String, title: String, order: Int, vararg branches: Branch) =
    Bough(id, title, order, branches.toList())

private fun branch(id: String, title: String, order: Int) =
    Branch(id, title, order, accent = "color.branch.$id", stems = emptyList())
