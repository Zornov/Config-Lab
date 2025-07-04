plugins {
    kotlin("jvm") version "2.2.0" apply false
    kotlin("plugin.serialization") version "2.2.0" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    group = "dev.zornov.config"

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
    }

    dependencies {
        add("implementation",
            "org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0")
        add("implementation",
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    }
}
