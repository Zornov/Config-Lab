plugins {
    id("dev.kache.config.sensitive") version "1.0"
    application
}

group = "dev.kache.config"
version = "1.0"

repositories {
    mavenLocal()
}

dependencies {
    // Core
    implementation(project(":config-core"))

    // Annotations
    implementation(project(":config-annotations"))

    // YAML formatter
    implementation(project(":config-format-yaml"))
}

application {
    mainClass.set("dev.kache.config.example.MainKt")
}