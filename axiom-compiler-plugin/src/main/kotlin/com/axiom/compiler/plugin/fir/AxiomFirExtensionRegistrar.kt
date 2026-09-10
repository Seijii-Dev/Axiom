package com.axiom.compiler.plugin.fir

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class AxiomFirExtensionRegistrar(private val messageCollector: MessageCollector) : FirExtensionRegistrar() {

    override fun ExtensionRegistrarContext.configurePlugin() {
        +::AxiomManifestCheckers
        +{ session: FirSession -> AxiomDescriptorGenerationExtension(session, messageCollector) }
    }
}
