package dev.kotree.build

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/** The 17 real roadmaps (docs/08 section 9.0). A roadmap edit that changes M changes this test and 08 section 9.0 together. */
class RealContentTest {
    private fun contentDir(): File {
        var d: File? = File("").absoluteFile
        while (d != null) {
            val c = File(d, "content")
            if (File(c, "foundation/_roadmap.md").isFile) return c
            d = d.parentFile
        }
        error("content/ not found above ${File("").absolutePath}")
    }

    @Test
    fun realRoadmapsParseAndValidate() {
        val c = loadContent(contentDir())
        assertEquals(17, c.roadmaps.size)
        assertEquals(BRANCHES.map { it.slug }, c.roadmaps.map { it.branch }.sortedBy { b -> BRANCHES.indexOfFirst { it.slug == b } })
        assertEquals(180, c.roadmaps.sumOf { it.stems.size })
        assertEquals(482, c.roadmaps.sumOf { r -> r.stems.sumOf { it.leaves.size } })
        assertEquals(20, c.roadmaps.sumOf { r -> r.stems.sumOf { s -> s.leaves.count { it.fruit } } })
        assertEquals(20, c.roadmaps.sumOf { it.fruitRows.size })

        val perBranch = c.roadmaps.associate { r ->
            r.branch to Triple(r.stems.sumOf { it.leaves.size }, r.stems.size, r.stems.sumOf { s -> s.leaves.count { it.fruit } })
        }
        val expected = mapOf(
            "foundation" to Triple(34, 6, 0), "kotlin" to Triple(65, 15, 2), "android-core" to Triple(37, 12, 2),
            "ui" to Triple(49, 14, 2), "concurrency" to Triple(32, 13, 1), "architecture-data" to Triple(33, 12, 1),
            "app-networking" to Triple(17, 9, 1), "kmp" to Triple(22, 10, 1), "dsa" to Triple(46, 16, 2),
            "daa" to Triple(22, 9, 1), "os" to Triple(23, 10, 1), "networking" to Triple(21, 10, 1),
            "testing" to Triple(22, 11, 1), "profiling" to Triple(16, 10, 1), "native-ndk" to Triple(11, 6, 1),
            "security" to Triple(15, 10, 1), "tools" to Triple(17, 7, 1),
        )
        assertEquals(expected, perBranch)

        assertEquals(emptyList(), validate(c, null).map { it.format() })
    }
}
