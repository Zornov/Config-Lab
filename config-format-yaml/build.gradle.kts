
dependencies {
    implementation(project(":config-annotations"))
    implementation(project(":config-core"))

    implementation("com.charleskorn.kaml:kaml:0.78.0")
    implementation(kotlin("reflect"))

    testImplementation(kotlin("test"))
}