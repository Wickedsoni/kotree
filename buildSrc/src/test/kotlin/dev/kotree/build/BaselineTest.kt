package dev.kotree.build

import java.io.File
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BaselineTest {
    private fun run(dir: File, vararg args: String) {
        val p = ProcessBuilder(listOf("git", "-c", "user.name=t", "-c", "user.email=t@t", "-c", "commit.gpgsign=false") + args)
            .directory(dir).redirectErrorStream(true).start()
        val text = p.inputStream.readBytes().toString(Charsets.UTF_8)
        assertEquals(0, p.waitFor(), "git ${args.toList()} failed: $text")
    }

    @Test
    fun v8WithTempGitRepo() {
        val repo = Files.createTempDirectory("kotree-git").toFile()
        run(repo, "init", "-q")
        val fx = File(Fx::class.java.getResource("/fixtures/good/content")!!.toURI())
        fx.copyRecursively(File(repo, "content"), overwrite = true)
        run(repo, "add", ".")
        run(repo, "commit", "-q", "-m", "baseline")
        run(repo, "branch", "-M", "main")

        val base = readBaselineLive(repo, "main")
        assertNull(base.warning)
        assertEquals(setOf("foundation/alpha/first-leaf"), base.live)

        // Unchanged content: no V8.
        val content = File(repo, "content")
        assertEquals(emptyList(), validate(loadContent(content), base.live).of("V8"))

        // Un-publish the live leaf: V8 fires.
        val leaf = File(content, "foundation/alpha/first-leaf.md")
        leaf.writeText(leaf.readText().replace("status: live", "status: shadow"))
        val vs = validate(loadContent(content), base.live).of("V8")
        assertEquals(1, vs.size, vs.map { it.format() }.toString())
        assertEquals("foundation/alpha/first-leaf", vs[0].id)

        // Delete (or rename) the leaf: V8 fires as removed.
        leaf.delete()
        val gone = validate(loadContent(content), base.live).of("V8")
        assertTrue(gone.any { "removed or renamed" in it.message })
    }

    @Test
    fun missingRefIsAWarningNotAFailure() {
        val repo = Files.createTempDirectory("kotree-git2").toFile()
        run(repo, "init", "-q")
        val b = readBaselineLive(repo, "origin/main")
        assertNull(b.live)
        assertNotNull(b.warning)
    }

    @Test
    fun notAGitRepoIsAWarning() {
        val dir = Files.createTempDirectory("kotree-nogit").toFile()
        val b = readBaselineLive(dir, "origin/main")
        assertNull(b.live)
        assertNotNull(b.warning)
    }
}
