plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("kapt")
}

group = "dev.kache.config.sensitive"
version = "1.0-SNAPSHOT"

gradlePlugin {
    plugins {
        create("sensitivePlugin") {
            id = "dev.kache.config.sensitive"
            implementationClass = "dev.kache.config.sensetive.gradle.SensitiveGradlePlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.2.0-RC")
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
}

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/src/main/kotlin/dev/kache/config/sensetive/gradle")
    outputs.dir(outputDir)

    doLast {
        val outputFile = outputDir.get().file("BuildConfig.kt").asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """
            package dev.kache.config.sensetive.gradle

            object BuildConfig {
                const val VERSION = "${project.version}"
            }
            """.trimIndent()
        )
    }
}

sourceSets.main {
    java.srcDirs(generateBuildConfig)
}

tasks.compileKotlin {
    dependsOn(generateBuildConfig)
}
