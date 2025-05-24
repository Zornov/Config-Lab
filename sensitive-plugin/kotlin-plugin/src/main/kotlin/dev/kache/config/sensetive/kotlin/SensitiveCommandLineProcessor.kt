package dev.kache.config.sensetive.kotlin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CommandLineProcessor::class)
class SensitiveCommandLineProcessor : CommandLineProcessor {
    companion object {
        const val PLUGIN_ID = "dev.kache.config.sensetive.kotlin"
    }

    override val pluginId: String = PLUGIN_ID
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()
}
