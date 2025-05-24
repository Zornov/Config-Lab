package dev.zornov.config.sensetive.kotlin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration

@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
class SensitiveK2Registrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true
    @OptIn(ExperimentalCompilerApi::class)
    override fun ExtensionStorage.registerExtensions(
        configuration: CompilerConfiguration
    ) {
        val collector: MessageCollector =
            configuration.get(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY)
                ?: MessageCollector.NONE
        collector.report(
            CompilerMessageSeverity.OUTPUT,
            "Sensitive plugin initialized"
        )

        IrGenerationExtension.registerExtension(
            SensitiveIrGenerationExtension(),
        )
    }

}