import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication

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
        }
    }
}

kotlin {
    configAsKobwebApplication("kotree")

    sourceSets {
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.kobwebx.markdown)
        }
    }
}
