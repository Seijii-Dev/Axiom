package com.axiom.plugin

import com.axiom.api.InternalAxiomApi
import com.axiom.api.data.file.KxFile
import com.axiom.api.data.file.providerKey
import com.axiom.api.lsp.LanguageServerProvider
import com.axiom.api.lsp.LanguageServerRegistration
import com.axiom.api.lsp.LanguageServerRegistry
import com.axiom.api.plugin.AxiomPlugin
import org.koin.core.annotation.Single
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Single
class LanguageServerRegistryImpl : LanguageServerRegistry {

    private data class RegistrationInfo(
        val id: String,
        val pattern: String,
        val provider: LanguageServerProvider,
        val plugin: AxiomPlugin?
    )

    private val registrations = ConcurrentHashMap<String, RegistrationInfo>()
    private val nextId = AtomicInteger(0)

    context(plugin: AxiomPlugin)
    override fun register(pattern: String, provider: LanguageServerProvider): LanguageServerRegistration {
        return doRegister(pattern, provider, plugin)
    }

    @InternalAxiomApi
    override fun registerInternal(pattern: String, provider: LanguageServerProvider): LanguageServerRegistration {
        return doRegister(pattern, provider, null)
    }

    private fun doRegister(
        pattern: String,
        provider: LanguageServerProvider,
        plugin: AxiomPlugin?
    ): LanguageServerRegistration {
        val id = nextId.getAndIncrement().toString()
        val info = RegistrationInfo(id, pattern, provider, plugin)
        registrations[id] = info
        return object : LanguageServerRegistration {
            override fun unregister() {
                this@LanguageServerRegistryImpl.unregister(id)
            }
        }
    }

    override fun unregister(id: String) {
        registrations.remove(id)
    }

    override fun getProviders(file: KxFile): List<LanguageServerRegistry.RegisteredProvider> {
        val key = file.providerKey
        return registrations.values
            .filter { it.pattern == key }
            .sortedBy { it.id.toInt() }
            .map { LanguageServerRegistry.RegisteredProvider(it.id, it.provider) }
    }
}
