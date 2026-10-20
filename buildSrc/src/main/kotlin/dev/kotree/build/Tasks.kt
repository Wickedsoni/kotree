package dev.kotree.build

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import java.io.File

/** Fails the build when a rule of docs/14 section 3.2 is broken. Has no outputs, so it always runs. */
abstract class ValidateContentTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val contentDir: DirectoryProperty

    /** V8 baseline, `-PbaselineRef=<ref>`; default `origin/main`. */
    @get:Input
    @get:Optional
    abstract val baselineRef: Property<String>

    @TaskAction
    fun run() {
        val dir = contentDir.get().asFile
        val ref = baselineRef.getOrElse("origin/main")
        val baseline = readBaselineLive(dir.parentFile, ref)
        baseline.warning?.let { logger.warn(it) }
        val violations = validate(loadContent(dir), baseline.live)
        if (violations.isNotEmpty()) {
            throw GradleException("validateContent: ${violations.size} violation(s)\n" + renderViolations(violations))
        }
        logger.lifecycle("validateContent: ok")
    }
}

/** Writes `GeneratedTreeData.kt` and `tree.json` from one in-memory model, deterministically (docs/14 section 3.4). */
abstract class GenerateTreeDataTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val contentDir: DirectoryProperty

    /** Source root; the file goes to `dev/kotree/site/tree/model/GeneratedTreeData.kt` below it. */
    @get:OutputDirectory
    abstract val kotlinOut: DirectoryProperty

    @get:OutputFile
    abstract val jsonOut: RegularFileProperty

    @TaskAction
    fun run() {
        val tree = buildTree(loadContent(contentDir.get().asFile))
        val root = kotlinOut.get().asFile
        root.deleteRecursively()
        writeUtf8(File(root, GENERATED_FILE_PATH), emitKotlin(tree))
        writeUtf8(jsonOut.get().asFile, emitJson(tree))
    }

    private fun writeUtf8(f: File, text: String) {
        f.parentFile.mkdirs()
        f.writeBytes(text.toByteArray(Charsets.UTF_8)) // no BOM, `\n` kept as written
    }
}
