package dev.zornov.config.sensetive.gradle

import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

class SensitiveGradlePlugin : KotlinCompilerPluginSupportPlugin {
    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        return project.provider { emptyList() }
    }

    override fun getCompilerPluginId(): String =
        "dev.kache.config.sensitive"

    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact(
            groupId    = "dev.kache.config.sensitive",
            artifactId = "kotlin-plugin",
            version    = BuildConfig.VERSION
        )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        true

}
