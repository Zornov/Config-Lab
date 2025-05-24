pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "Config-lab"
include("config-annotations")
include("config-core")
include("config-format-yaml")

include("sensitive-plugin")
include("sensitive-plugin:gradle-plugin")
include("sensitive-plugin:kotlin-plugin")