package com.axiom.api

import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import com.axiom.api.plugin.PluginService
import com.axiom.api.plugin.pluginService
import com.axiom.api.ui.Content
import com.axiom.api.ui.ScreenId
import com.axiom.api.ui.ScreenRegistration
import com.axiom.core.LocalApp

/**
 * Handles navigation within the Axiom application.
 *
 * This service allows plugins to trigger navigation to different screens or parts of the app,
 * including built-in destinations like Home and Settings, as well as custom screens.
 *
 * ### Example
 * ```kotlin
 * val navigator: Navigator by plugin()
 *
 * fun goToSettings() {
 *     navigator.navigateTo(NavDestination.Settings)
 * }
 * ```
 */
interface Navigator : PluginService {

    /**
     * Navigates to the specified [destination].
     */
    fun navigateTo(destination: NavDestination)

    /**
     * Navigates back to the previous screen in the stack.
     */
    fun navigateBack()

    /**
     * Registers the [content] for [screenId] and navigates to it.
     *
     * Unlike [navigateTo], which only opens a screen that was already registered via the
     * [ScreenRegistry][com.axiom.api.ui.ScreenRegistry], this lets a plugin start a screen
     * directly from the given composable. Since the [content] is a closure, it can capture any
     * data the screen needs (a file, a URI, a result object, etc.).
     *
     * The screen is registered as a *transient* screen: axiom auto-unregisters it when its
     * navigation entry is popped, so no cleanup is required. It can also be removed early via the
     * returned [ScreenRegistration].
     *
     * ### Example
     * ```kotlin
     * val navigator: Navigator by plugin()
     *
     * navigator.openScreen(ScreenId("com.example.html.preview")) {
     *     HtmlPreviewScreen(uri)
     * }
     * ```
     */
    fun openScreen(screenId: ScreenId, content: Content): ScreenRegistration
}

/**
 * A collection of predefined [ScreenId]s for common application screens.
 */
object SpecialScreens {
    /** The home screen ID. */
    val Home = ScreenId("<axiom-home>")

    /** The settings screen ID. */
    val Settings = ScreenId("<axiom-settings>")

    /** The terminal screen ID. */
    val Terminal = ScreenId("<axiom-terminal>")
}

/**
 * Convenience extension to navigate to a specific [ScreenId].
 *
 * @param screenId The ID of the screen to navigate to.
 */
fun Navigator.navigateTo(screenId: ScreenId) = navigateTo(NavDestination.Custom(screenId))

/**
 * CompositionLocal that provides the current [Navigator] instance.
 *
 * By default, it retrieves the [Navigator] from the current [LocalApp].
 */
val LocalNavigator = compositionLocalWithComputedDefaultOf {
    LocalApp.currentValue.pluginService(Navigator::class)
}
