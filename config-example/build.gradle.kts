plugins {
    id("dev.zornov.config.sensitive") version "1.0"
    application
}

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
    implementation(project(":config-format:config-format-yaml"))

    // Json formatter
    implementation(project(":config-format:config-format-json"))
}

application {
    mainClass.set("dev.zornov.config.example.MainKt")
}