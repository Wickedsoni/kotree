plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    // buildSrc does not see the root version catalog, so versions are literal (docs/14 section 3.4).
    implementation("org.yaml:snakeyaml:2.7")
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
