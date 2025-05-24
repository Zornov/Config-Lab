plugins {
    `maven-publish`
}

group = "dev.kache.config.format"
version = "1.0"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation(project(":config-annotations"))
    implementation(project(":config-core"))

    implementation("com.charleskorn.kaml:kaml:0.78.0")
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
