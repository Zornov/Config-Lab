plugins {
    `maven-publish`
}

version = "1.0"

dependencies {
    implementation(project(":config-annotations"))
    implementation(kotlin("reflect"))

    testImplementation(kotlin("test-junit"))
}

publishing {
    publications {
        create<MavenPublication>("configCore") {
            from(components["java"])
            artifactId = "core"
            groupId    = project.group.toString()
            version    = project.version.toString()

            pom {
                name.set("core")
                description.set("Core library for sensitive configuration")
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
