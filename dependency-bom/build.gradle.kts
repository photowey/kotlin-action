plugins {
    `java-platform`
    `maven-publish`
}

description = "dependency management for kotlin-action project"

dependencies {
    constraints {
        api(libs.kotlinx.serialization.json)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["javaPlatform"])
            pom {
                packaging = "pom"
                name.set("dependency-bom")
                description.set("Bill of Materials for kotlin-action project")
            }
        }
    }
}