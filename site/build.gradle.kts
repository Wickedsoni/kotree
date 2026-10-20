import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
}

group = "dev.kotree.site"
version = "1.0-SNAPSHOT"

kobweb {
    app {
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

kotlin {
    configAsKobwebApplication("kotree")

    sourceSets {
        jsMain {
            kotlin.srcDir(generateTreeData.flatMap { it.kotlinOut })
        }
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.kobwebx.markdown)
        }
    }
}
