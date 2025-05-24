plugins {
    kotlin("kapt")
    `maven-publish`
}

group = "dev.kache.config.sensitive"
version = "1.0"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.1.21-321")
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
