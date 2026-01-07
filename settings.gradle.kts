pluginManagement {
    val props = java.util.Properties().apply {
        file("gradle.properties").inputStream().use { load(it) }
    }

    fun propOf(key: String): String = props.getProperty(key)

    fun requireProperty(key: String): String {
        return propOf(key)
            ?: error("Property '$key' is missing in gradle.properties")
    }

    val kotlinVersion = requireProperty("kotlin.version")
    val kotlinPluginSpringVersion = requireProperty("plugin.spring.version")
    val springBootVersion = requireProperty("org.springframework.boot.version")
    val springDependencyManagementVersion = requireProperty("io.spring.dependency-management.version")
    val spotlessVersion = requireProperty("com.diffplug.spotless.version")

    plugins {
        kotlin("jvm")
        kotlin("plugin.spring") version kotlinPluginSpringVersion
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        id("com.diffplug.spotless") version spotlessVersion
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
            }
        }
    }

    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://maven.aliyun.com/repository/gradle-plugin")
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlin-action"
include("app", "dependency-bom", "tools")