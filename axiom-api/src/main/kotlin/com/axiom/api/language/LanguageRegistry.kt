package com.axiom.api.language

import com.axiom.api.plugin.AxiomPlugin
import com.axiom.api.plugin.PluginService

interface LanguageRegistry : PluginService {

    context(plugin: AxiomPlugin)
    fun register(
        descriptor: LanguageDescriptor,
        grammarProvider: LanguageGrammarProvider,
        queries: QueryProvider,
        theme: LanguageThemeProvider? = null,
    ): LanguageRegistration

    fun unregister(id: String)

    fun getDescriptor(name: String): LanguageDescriptor?

    fun getExtensions(): Map<String, String>

    fun getFileNames(): Map<String, String>
}
