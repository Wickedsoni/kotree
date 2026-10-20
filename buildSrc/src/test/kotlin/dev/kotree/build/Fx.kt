package dev.kotree.build

import java.io.File
import java.nio.file.Files

/** A copy of `fixtures/good/content` in a temp dir, mutated per test. The good fixture itself has zero violations. */
class Fx {
    val content: File = Files.createTempDirectory("kotree-fx").toFile().resolve("content")

    init {
        val src = File(Fx::class.java.getResource("/fixtures/good/content")!!.toURI())
        src.copyRecursively(content, overwrite = true)
        // A Windows checkout may hold CRLF; tests edit by exact text, so normalise to LF.
        content.walkTopDown().filter { it.isFile }.forEach { f -> f.writeText(f.readText().replace("\r\n", "\n")) }
    }

    fun file(rel: String): File = File(content, rel)

    /** Replaces [from] with [to] in [rel]; fails the test setup if [from] is not there. */
    fun edit(rel: String, from: String, to: String): Fx {
        val f = file(rel)
        val text = f.readText()
        check(text.contains(from)) { "fixture edit: `$from` not found in $rel" }
        f.writeText(text.replace(from, to))
        return this
    }

    fun write(rel: String, text: String): Fx {
        val f = file(rel)
        f.parentFile.mkdirs()
        f.writeText(text)
        return this
    }

    fun violations(baselineLive: Set<String>? = null): List<Violation> = validate(loadContent(content), baselineLive)

    companion object {
        const val RM = "foundation/_roadmap.md"
        const val FIRST = "foundation/alpha/first-leaf.md"
        const val SECOND = "foundation/alpha/second-leaf.md"
    }
}

fun List<Violation>.rules(): Set<String> = map { it.rule }.toSet()

fun List<Violation>.of(rule: String): List<Violation> = filter { it.rule == rule }
