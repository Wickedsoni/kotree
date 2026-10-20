package dev.kotree.build

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/** [live] is null when the baseline cannot be read; then [warning] says why (V8 is a warning, not a failure). */
data class Baseline(val live: Set<String>?, val warning: String?)

private data class GitResult(val code: Int, val out: String, val err: String)

private fun git(repo: File, vararg args: String): GitResult? = try {
    val p = ProcessBuilder(listOf("git") + args).directory(repo).start()
    p.outputStream.close()
    val out = StringBuilder()
    val err = StringBuilder()
    val tOut = Thread { out.append(p.inputStream.readBytes().toString(Charsets.UTF_8)) }.also { it.start() }
    val tErr = Thread { err.append(p.errorStream.readBytes().toString(Charsets.UTF_8)) }.also { it.start() }
    if (!p.waitFor(60, TimeUnit.SECONDS)) {
        p.destroyForcibly()
        null
    } else {
        tOut.join()
        tErr.join()
        GitResult(p.exitValue(), out.toString(), err.toString())
    }
} catch (e: IOException) {
    null
}

/**
 * Leaf IDs that were `status: live` at [ref] (docs/14 section 3.2 V8, D16: git history, not a list file).
 * Runs `git grep -lE '^status:[[:space:]]*live' <ref> -- <content glob for leaf files>` in [repoDir].
 */
fun readBaselineLive(repoDir: File, ref: String): Baseline {
    val verify = git(repoDir, "rev-parse", "--verify", "--quiet", "$ref^{commit}")
        ?: return Baseline(null, "V8 skipped: git is not available")
    if (verify.code != 0) return Baseline(null, "V8 skipped: baseline ref `$ref` not found (shallow clone or no git repository)")
    val grep = git(repoDir, "grep", "-lE", "^status:[[:space:]]*live", ref, "--", "content/*/*/*.md")
        ?: return Baseline(null, "V8 skipped: git grep did not finish")
    if (grep.code > 1) return Baseline(null, "V8 skipped: git grep failed: ${grep.err.trim()}")
    val live = grep.out.lineSequence().map { it.trim() }.filter { it.isNotEmpty() }.mapNotNull { line ->
        val path = line.substringAfter("$ref:", "")
        val parts = path.removePrefix("content/").removeSuffix(".md").split("/")
        if (path.endsWith(".md") && parts.size == 3 && !parts[2].startsWith("_")) parts.joinToString("/") else null
    }.toSortedSet()
    return Baseline(live, null)
}
