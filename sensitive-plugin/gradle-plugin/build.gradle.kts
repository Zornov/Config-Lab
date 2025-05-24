plugins {
    `java-gradle-plugin`
    `maven-publish`
    kotlin("kapt")
}

group = "dev.kache.config.sensitive"
version = "1.0"

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

publishing {
    publications {
        create<MavenPublication>("plugin") {
            from(components["java"])
            artifactId = "gradle-plugin"

            pom {
                name.set("gradle-plugin")
                description.set("Gradle plugin for sensitive configuration")
                url.set("https://github.com/KachVev/Config-Lab")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("KachVev")
                        name.set("Sasha Kachvev")
                        email.set("kachevnikov.dev@gmail.com")
                    }
                }
            }
        }
    }
    repositories {
        mavenLocal()

        maven {
            name = "GitHubPackages"
            url  = uri("https://maven.pkg.github.com/KachVev/Config-Lab")
            credentials {
                username = findProperty("gpr.user") as String
                password = findProperty("gpr.key")  as String
            }
            mavenContent { snapshotsOnly() }
        }
    }
}
