plugins {
    `maven-publish`
}

group = "dev.zornov.config.format"
version = "1.0"

dependencies {
    implementation(project(":config-annotations"))
    implementation(project(":config-core"))

    implementation("com.charleskorn.kaml:kaml:0.83.0")
    implementation(kotlin("reflect"))

    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("configYml") {
            from(components["java"])
            artifactId = "yml"
            groupId    = project.group.toString()
            version    = project.version.toString()

            pom {
                name.set("yml")
                description.set("YML support library for sensitive configuration")
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
