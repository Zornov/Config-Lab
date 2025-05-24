plugins {
    kotlin("kapt")
    `maven-publish`
}

group = "dev.kache.config.sensitive"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.1.21")
    compileOnly("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
}

publishing {
    publications {
        create<MavenPublication>("kotlinPlugin") {
            from(components["java"])
            artifactId = "kotlin-plugin"
        }
    }
    repositories {
        mavenLocal()
    }
}