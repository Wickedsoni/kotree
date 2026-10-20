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
