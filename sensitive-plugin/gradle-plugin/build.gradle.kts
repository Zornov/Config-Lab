plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("kapt")
}

group = "dev.zornov.config.sensitive"
version = "1.0"

gradlePlugin {
    plugins {
        create("sensitivePlugin") {
            id = "dev.zornov.config.sensitive"
            implementationClass = "dev.zornov.config.sensetive.gradle.SensitiveGradlePlugin"
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.2.0-RC3")
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
}

val generateBuildConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/src/main/kotlin/dev/zornov/config/sensetive/gradle")
    outputs.dir(outputDir)

    doLast {
        val outputFile = outputDir.get().file("BuildConfig.kt").asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """
            package dev.zornov.config.sensetive.gradle

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

publishing {
    publications {
        create<MavenPublication>("plugin") {
            from(components["java"])
            artifactId = "gradle-plugin"

            pom {
                name.set("gradle-plugin")
                description.set("Gradle plugin for sensitive configuration")
                url.set("https://github.com/Zornoov/Config-Lab")
                developers {
                    developer {
                        id.set("Zornoov")
                        name.set("Sasha Zornoov")
                    }
                }
            }
        }
    }
    repositories {
        mavenLocal()

        maven {
            name = "GitHubPackages"
            url  = uri("https://maven.pkg.github.com/Zornoov/Config-Lab")
            credentials {
                username = findProperty("gpr.user") as String
                password = findProperty("gpr.key")  as String
            }
            mavenContent { snapshotsOnly() }
        }
    }
}
