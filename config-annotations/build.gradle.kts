plugins {
    `maven-publish`
}

version = "1.0"

dependencies {
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("reflect"))
}

publishing {
    publishing {
        publications {
            create<MavenPublication>("configAnnotations") {
                from(components["java"])
                artifactId = "annotations"
                groupId    = project.group.toString()
                version    = project.version.toString()

                pom {
                    name.set("annotations")
                    description.set("Annotation library for sensitive configuration")
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
}
