plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // buildSrc does not see the root version catalog, so versions are literal (docs/14 section 3.4).
    implementation("org.yaml:snakeyaml:2.7")
    // Markdown to HTML for lesson prose (ADR 005 section 1). Latest release on Maven Central, checked 21 Oct 2026.
    implementation("org.commonmark:commonmark:0.30.1")
    implementation("org.commonmark:commonmark-ext-gfm-tables:0.30.1")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
