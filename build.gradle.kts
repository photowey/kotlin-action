plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
    id("com.diffplug.spotless") apply false
}

val mavenRepo: String = project.findProperty("mavenRepo") as String?
    ?: System.getenv("MAVEN_REPO")
    ?: throw GradleException("mavenRepo not set in gradle.properties or MAVEN_REPO env var")

val jdkVersion: Int = (project.findProperty("java.version") as String?)
    ?.toIntOrNull()
    ?: 25

allprojects {
    group = property("projectGroup") as String
    version = property("projectVersion") as String

    extra["jdkVersion"] = jdkVersion

    repositories {
        maven {
            url = uri(uri(File(mavenRepo).toURI()))
            isAllowInsecureProtocol = true
        }
        mavenLocal()
        maven {
            url = uri("https://maven.aliyun.com/repository/google")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }

        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            licenseHeaderFile(rootProject.file("codequality/license-heaader.txt"))
            target("src/main/java/**/*.java", "src/test/java/**/*.java")
        }

        kotlin {
            licenseHeaderFile(rootProject.file("codequality/license-heaader.txt"))
            target("src/main/kotlin/**/*.kt", "src/test/kotlin/**/*.kt")
        }
    }

    tasks.named("build").configure {
        dependsOn(tasks.named("spotlessCheck"))
    }
}

configure(listOf(project(":tools"), project(":dependency-bom"))) {
    apply(plugin = "maven-publish")

    configure<PublishingExtension> {
        repositories {
            val isSnapshot = version.toString().endsWith("-SNAPSHOT")
            maven {
                name = if (isSnapshot) "snapshot" else "release"
                url = if (isSnapshot) {
                    uri(uri(File(mavenRepo).toURI()))
                } else {
                    uri(uri(File(mavenRepo).toURI()))
                }

                /*
                credentials {
                    val usernamePropertyKey = if (isSnapshot) "nexusUsernameSnapshot" else "nexusUsernameRelease"
                    val usernameEnvKey = if (isSnapshot) "NEXUS_USERNAME_SNAPSHOT" else "NEXUS_USERNAME_RELEASE"

                    val passwordPropertyKey = if (isSnapshot) "nexusUsernameSnapshot" else "nexusUsernameRelease"
                    val passwordEnvKey = if (isSnapshot) "NEXUS_PASSWORD_SNAPSHOT" else "NEXUS_PASSWORD_RELEASE"

                    username = project.findProperty(usernamePropertyKey) as String? ?: System.getenv(usernameEnvKey)
                    password = project.findProperty(passwordPropertyKey) as String? ?: System.getenv(passwordEnvKey)
                }
                */

                isAllowInsecureProtocol = true
            }
        }
    }
}
