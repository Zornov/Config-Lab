plugins {
    kotlin("kapt")
    `maven-publish`
}

group = "dev.zornov.config.sensitive"
version = "1.0"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.1.21-322")
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
}

publishing {
    publications {
        create<MavenPublication>("kotlinPlugin") {
            from(components["java"])
            artifactId = "kotlin-plugin"

            pom {
                name.set("kotlin-plugin")
                description.set("Kotlin compiler plugin for …")
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
