package com.axiom.api.plugin

import android.content.Context
import android.content.res.Resources
import com.axiom.api.Navigator
import com.axiom.api.data.fs.FileSystem
import com.axiom.api.data.terminal.TerminalManager
import com.axiom.api.service.Fonts
import com.axiom.api.service.Logger
import com.axiom.api.service.PluginLogger
import com.axiom.api.service.Settings
import com.axiom.api.service.Tabs
import com.axiom.api.ui.ScreenRegistry
import com.axiom.api.ui.ToolbarRegistry
import com.axiom.core.App
import java.io.File

/**
 * Provides the context in which a plugin operates, granting access to the core application and runtime services.
 */
interface PluginContext : PluginRuntimeService {

    /**
     * The core application instance.
     */
    val app: App

    /**
     * The unique identifier of the plugin.
     */
    val pluginId: String

    /**
     * A directory exclusively owned by this plugin for persistent data storage.
     *
     * The directory is created when first accessed. Plugins may use it to store
     * configuration, cache, or any other private data. The host guarantees that
     * no other plugin can access this directory.
     *
     * ### Example
     * ```kotlin
     * val configFile = dataDir.resolve("config.json")
     * configFile.writeText(serializedState)
     * ```
     */
    val dataDir: File

    /**
     * The plugin's own [Resources], backed by its APK's compiled resource table
     * (`resources.arsc`).
     *
     * Unlike the host's resources, this table contains the exact resource IDs generated
     * by the plugin's own module, so references like `R.drawable.x`, `R.string.y` and
     * `R.style.z` resolve against the plugin's resources instead of the axiom's.
     *
     * Axiom keeps this table in sync with the app's configuration (locale, density,
     * night mode), so qualifier-aware lookups behave like a regular Android app.
     *
     * ### Example
     * ```kotlin
     * val label = context.resources.getString(R.string.plugin_title)
     * val icon = context.resources.getDrawable(R.drawable.plugin_icon, null)
     * ```
     */
    val resources: Resources

    /**
     * An Android [Context] whose [Context.getResources] and [Context.getAssets] point at
     * this plugin's own resources.
     *
     * Provide it to Compose so resource-backed APIs resolve against the plugin:
     * ```kotlin
     * CompositionLocalProvider(LocalContext provides context) {
     *     // painterResource(R.drawable.x), stringResource(R.string.y) now resolve
     * }
     * ```
     *
     * @see withResources
     */
    val context: Context
}

/**
 * Retrieves a service of the specified type [T] from the plugin context.
 *
 * @param T The type of the [PluginService] to retrieve.
 * @return The requested service instance.
 *
 * @see Settings
 * @see FileSystem
 * @see Fonts
 * @see Tabs
 * @see TerminalManager
 * @see ScreenRegistry
 * @see ToolbarRegistry
 * @see Navigator
 */
inline fun <reified T : PluginService> PluginContext.service(): T {
    val service = app.pluginService(T::class)
    return if (service is Logger && T::class == Logger::class) {
        PluginLogger(service, pluginId) as T
    } else {
        service
    }
}
