package com.axiom.compiler.plugin

import com.axiom.compiler.plugin.fir.AxiomFirExtensionRegistrar
import com.axiom.compiler.plugin.ir.AxiomIrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

class AxiomCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override val pluginId = BuildConfig.KOTLIN_PLUGIN_ID
    override val supportsK2 = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val descriptorOutputDir = configuration[AxiomConfigurationKeys.DESCRIPTOR_OUTPUT_DIR]
        val descriptorIcon = configuration[AxiomConfigurationKeys.DESCRIPTOR_ICON]
        val messageCollector = configuration[CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY]
            ?: MessageCollector.NONE

        FirExtensionRegistrarAdapter.registerExtension(AxiomFirExtensionRegistrar(messageCollector))
        IrGenerationExtension.registerExtension(
            AxiomIrGenerationExtension(
                descriptorOutputDir = descriptorOutputDir,
                descriptorIcon = descriptorIcon,
                messageCollector = messageCollector
            )
        )
    }
}
