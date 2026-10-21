import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
}

group = "dev.kotree.site"
version = "1.0-SNAPSHOT"

// Live lesson routes for the export (ADR 005 section 3). A ValueSource, so a new live leaf file changes the routes.
val liveRoutes: List<String> = providers.of(dev.kotree.build.LiveLessonRoutes::class) {
    parameters.contentDir.set(rootProject.layout.projectDirectory.dir("content"))
}.get()

kobweb {
    app {
        export {
            liveRoutes.forEach { addExtraRoute(it, exportPath = it.removePrefix("/") + ".html") }
        }
        index {
            description.set("Kotree: learn Android and CS theory on one tree.")
            head.add {
                // Self-hosted fonts (docs/02 section 3). Preload only the UI font; the code font loads on demand.
                link(rel = "preload", href = "/fonts/roboto-flex-v1.woff2") {
                    attributes["as"] = "font"
                    attributes["type"] = "font/woff2"
                    attributes["crossorigin"] = "anonymous"
                }
                link(rel = "stylesheet", href = "/fonts/fonts.css")
            }
        }
    }
}

val validateContent = tasks.register<dev.kotree.build.ValidateContentTask>("validateContent") {
    group = "kotree"
    description = "Checks /content against the rules V1-V12 (docs/14 section 3.2)."
    contentDir.set(rootProject.layout.projectDirectory.dir("content"))
    providers.gradleProperty("baselineRef").orNull?.let { baselineRef.set(it) }
}

val generateTreeData = tasks.register<dev.kotree.build.GenerateTreeDataTask>("generateTreeData") {
    group = "kotree"
    description = "Writes GeneratedTreeData.kt and build/tree.json from /content."
    dependsOn(validateContent)
    contentDir.set(rootProject.layout.projectDirectory.dir("content"))
    kotlinOut.set(layout.buildDirectory.dir("generated/kotree/kotlin"))
    jsonOut.set(layout.buildDirectory.file("tree.json"))
}

// Run after the export (docs/07 section 6.4). The app never fetches this file.
val copyTreeJson = tasks.register<Copy>("copyTreeJson") {
    group = "kotree"
    description = "Copies build/tree.json to .kobweb/site/tree.json (run after the export)."
    from(generateTreeData.flatMap { it.jsonOut })
    into(layout.projectDirectory.dir(".kobweb/site"))
}

// Preview of drafts (shadow leaves) is ON for a dev run (kobwebStart, kobweb run) and never in an export.
// Override with -Pkotree.preview=true|false. An export always wins: drafts must not ship.
val requestedTasks = gradle.startParameter.taskNames
val exporting = requestedTasks.any { it.contains("export", ignoreCase = true) }
val devRun = requestedTasks.any { Regex("kobwebStart|kobwebRun|BrowserDevelopmentRun|BrowserProductionRun").containsMatchIn(it) }
val previewOn = !exporting && (providers.gradleProperty("kotree.preview").map { it.toBoolean() }.orNull ?: devRun)

val renderLessons = tasks.register<dev.kotree.build.RenderLessonsTask>("renderLessons") {
    group = "kotree"
    description = "Renders content leaves to /lessons/<id>.json, static bodies, LessonConfig.kt and sitemap.xml (ADR 005)."
    dependsOn(validateContent)
    contentDir.set(rootProject.layout.projectDirectory.dir("content"))
    preview.set(previewOn)
    resourcesOut.set(layout.buildDirectory.dir("generated/kotree/resources"))
    previewOut.set(layout.buildDirectory.dir("generated/kotree/preview"))
    staticOut.set(layout.buildDirectory.dir("kotree/lessons-static"))
    kotlinOut.set(layout.buildDirectory.dir("generated/kotree/lesson-kotlin"))
    sitemapOut.set(layout.buildDirectory.file("kotree/sitemap.xml"))
}

// Run after the export and copyTreeJson (docs/07 section 6.7).
val emitLessonPages = tasks.register<dev.kotree.build.EmitLessonPagesTask>("emitLessonPages") {
    group = "kotree"
    description = "Bakes head tags, static prose and JSON into every exported live lesson page; writes sitemap.xml."
    contentDir.set(rootProject.layout.projectDirectory.dir("content"))
    staticDir.set(renderLessons.flatMap { it.staticOut })
    lessonsJsonDir.set(renderLessons.flatMap { it.resourcesOut.dir("public/lessons") })
    sitemapFile.set(renderLessons.flatMap { it.sitemapOut })
    siteDir.set(layout.projectDirectory.dir(".kobweb/site"))
    outputs.upToDateWhen { false }
}

tasks.register<dev.kotree.build.VerifyLessonExportTask>("verifyLessonExport") {
    group = "kotree"
    description = "Fails when .kobweb/site does not match the live leaves (and when any draft is in it)."
    contentDir.set(rootProject.layout.projectDirectory.dir("content"))
    siteDir.set(layout.projectDirectory.dir(".kobweb/site"))
    outputs.upToDateWhen { false }
}

kotlin {
    configAsKobwebApplication("kotree")

    // ADR 008 WP0: unit tests are pure Kotlin, so they run on Node (jsNodeTest). No browser or Karma needed in CI.
    js { nodejs() }

    sourceSets {
        jsMain {
            kotlin.srcDir(generateTreeData.flatMap { it.kotlinOut })
            kotlin.srcDir(renderLessons.flatMap { it.kotlinOut })
            resources.srcDir(renderLessons.flatMap { it.resourcesOut })
            if (previewOn) resources.srcDir(renderLessons.flatMap { it.previewOut })
        }
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
        }
        jsTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
