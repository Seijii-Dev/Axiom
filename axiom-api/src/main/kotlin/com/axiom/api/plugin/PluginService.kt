@file:OptIn(UnsafeGlobalAccess::class)

package com.axiom.api.plugin

import androidx.annotation.RestrictTo
import com.axiom.api.InternalAxiomApi
import com.axiom.api.service.Logger
import com.axiom.api.service.PluginLogger
import com.axiom.core.App
import com.axiom.core.Global
import com.axiom.core.unsafe.GlobalApp
import com.axiom.core.unsafe.UnsafeGlobalAccess
import java.util.WeakHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents a globally available service within the Axiom ecosystem.
 *
 * `PluginService`s are typically singleton-like components that provide core functionality
 * such as file system access, settings management, or UI registries. They are registered
 * globally and can be accessed from anywhere in the application.
 *
 * ### Accessing a PluginService
 *
 * 1. **Using the `plugin()` delegate (Recommended):**
 *    ```kotlin
 *    val settings: Settings by plugin()
 *    ```
 *
 * 2. **From the `App` instance:**
 *    ```kotlin
 *    val settings = app.pluginService<Settings>()
 *    ```
 *
 * 3. **From a `PluginContext`:**
 *    ```kotlin
 *    val settings = context.service<Settings>()
 *    ```
 *
 * @see Global
 * @see plugin
 */
interface PluginService : Global

/**
 * Represents a service that is specific to a plugin's runtime instance.
 *
 * Unlike [PluginService], which is global, a `PluginRuntimeService` is unique to each
 * [AxiomPlugin] instance. These services provide context and lifecycle information
 * specifically for the plugin they are associated with.
 *
 * Examples of runtime services include:
 * - [PluginContext]
 * - [PluginLifecycleOwner]
 * - [PluginScope]
 * - [PluginInfo]
 *
 * ### Accessing a PluginRuntimeService
 *
 * These services are typically accessed via the `runtime()` delegate on a [AxiomPlugin] instance:
 * ```kotlin
 * val context: PluginContext by runtime()
 * ```
 *
 * @see runtime
 * @see PluginRuntimeRegistry
 */
interface PluginRuntimeService

/**
 * Internal registry that manages and provides access to [PluginRuntimeService]s for each plugin.
 *
 * This registry is responsible for mapping a [AxiomPlugin] instance to its respective
 * runtime services. It is used primarily by the [runtime] delegate.
 */
@InternalAxiomApi
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface PluginRuntimeRegistry : Global {

    /**
     * Retrieves the [PluginRuntimeService] of type [type] for the given [plugin].
     */
    fun <T : PluginRuntimeService> service(
        plugin: AxiomPlugin,
        type: KClass<T>
    ): T
}

/**
 * Retrieves a [PluginService] of type [T] from the [App] instance.
 */
inline fun <reified T : PluginService> App.pluginService(type: KClass<T>): T = global(type)

/**
 * A delegate for accessing a [PluginService] from any class.
 *
 * @param T The type of [PluginService] to retrieve.
 */
inline fun <reified T : PluginService> plugin() = PluginServiceDelegate(T::class)

/**
 * A delegate for accessing a [PluginRuntimeService] associated with a [AxiomPlugin].
 *
 * Runtime services are provided by the axiom only after a plugin is constructed and
 * registered. **Plugin constructors must not access runtime services**. resolve them
 * lazily (e.g. `val context: PluginContext by runtime()`) or inside
 * [AxiomPlugin.onLoad] / [AxiomPlugin.onStart].
 *
 * @param T The type of [PluginRuntimeService] to retrieve.
 */
inline fun <reified T : PluginRuntimeService> runtime() = PluginRuntimeDelegate(T::class)

class PluginRuntimeDelegate<T : PluginRuntimeService>(
    private val clazz: KClass<T>
) : ReadOnlyProperty<AxiomPlugin, T> {

    private val cache = WeakHashMap<AxiomPlugin, T>()

    @OptIn(InternalAxiomApi::class)
    override fun getValue(thisRef: AxiomPlugin, property: KProperty<*>): T {
        return cache.getOrPut(thisRef) {
            val registry = GlobalApp.global<PluginRuntimeRegistry>()
            registry.service(thisRef, clazz)
        }
    }
}

class PluginServiceDelegate<T : PluginService>(
    private val clazz: KClass<T>
) : ReadOnlyProperty<Any?, T> {

    private val service by lazy {
        GlobalApp.global(clazz)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (thisRef is AxiomPlugin && service is Logger && clazz == Logger::class) {
            @Suppress("UNCHECKED_CAST")
            return PluginLogger(service as Logger) { thisRef.info.id } as T
        }
        return service
    }
}
