plugins {
    id("dev.kache.config.sensitive") version "1.8-SNAPSHOT"
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(project(":config-annotations"))
    implementation(kotlin("reflect"))

    testImplementation(kotlin("test-junit"))
}
