package dev.kotree.build

/** The fixed tree (docs/00 section 5, docs/03 section 1). The only place that lists boughs and branches. */
data class BoughDef(val slug: String, val title: String)

data class BranchDef(val slug: String, val title: String, val bough: String)

val BOUGHS: List<BoughDef> = listOf(
    BoughDef("roots", "Foundation"),
    BoughDef("kotlin-android", "Kotlin and Android"),
    BoughDef("architecture-kmp", "Architecture and KMP"),
    BoughDef("cs-theory", "CS theory"),
    BoughDef("craft", "Craft"),
)

/** In the order of docs/00 section 5. */
val BRANCHES: List<BranchDef> = listOf(
    BranchDef("foundation", "Foundation", "roots"),
    BranchDef("kotlin", "Kotlin language", "kotlin-android"),
    BranchDef("android-core", "Android core and lifecycles", "kotlin-android"),
    BranchDef("ui", "UI (Compose and Views)", "kotlin-android"),
    BranchDef("concurrency", "Coroutines, Flow, threads", "kotlin-android"),
    BranchDef("architecture-data", "Architecture and data", "architecture-kmp"),
    BranchDef("app-networking", "Networking inside apps", "architecture-kmp"),
    BranchDef("kmp", "Kotlin Multiplatform", "architecture-kmp"),
    BranchDef("dsa", "Data structures and algorithms", "cs-theory"),
    BranchDef("daa", "Design and analysis of algorithms", "cs-theory"),
    BranchDef("os", "Operating systems", "cs-theory"),
    BranchDef("networking", "Computer networking", "cs-theory"),
    BranchDef("testing", "Testing", "craft"),
    BranchDef("profiling", "Profiling and optimisation", "craft"),
    BranchDef("native-ndk", "Native layer (NDK, JNI, ART)", "craft"),
    BranchDef("security", "Security", "craft"),
    BranchDef("tools", "Tools and ecosystem", "craft"),
)

fun branchDef(slug: String): BranchDef? = BRANCHES.firstOrNull { it.slug == slug }

/** docs/03 section 7.3. Any name starting with `_` is reserved too (except `_templates`). */
val RESERVED_FIRST_SEGMENTS: Set<String> = setOf(
    "assets", "fonts", "images", "kitchen-sink", "about", "progress", "api", "tree.json", "404", "lessons", "sitemap.xml",
)

val SLUG_REGEX = Regex("^[a-z0-9]+(-[a-z0-9]+)*$")
