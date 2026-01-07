plugins {
    kotlin("jvm")
    `maven-publish`
}

// ----------------------------------------------------------------

description = "tools module for kotlin-action project"

// ----------------------------------------------------------------

val jdkVersion: Int by project.extra

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(jdkVersion)
    }

    withSourcesJar()
    withJavadocJar()
}

// ----------------------------------------------------------------

dependencies {
    api(platform(project(":dependency-bom")))
    api("org.jetbrains.kotlinx:kotlinx-serialization-json")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

// ----------------------------------------------------------------

tasks.withType<Test> {
    useJUnitPlatform()
}

// ----------------------------------------------------------------

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "tools"
            from(components["java"])
        }
    }
}