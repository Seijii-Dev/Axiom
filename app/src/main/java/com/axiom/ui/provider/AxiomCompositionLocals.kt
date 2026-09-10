package com.axiom.ui.provider

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiom.core.App
import com.axiom.core.LocalApp
import com.axiom.data.editor.LocalAxiomEditorColorScheme
import com.axiom.data.editor.rememberEditorColorScheme
import com.axiom.api.data.preferences.AppSettings
import com.axiom.api.data.preferences.AppTheme
import com.axiom.api.data.preferences.LocalAppSettings
import com.axiom.api.language.LanguageRegistry
import com.axiom.data.preferences.SettingsRepository
import com.axiom.event.eventBus
import com.axiom.language.LanguageRegistryImpl
import com.axiom.ui.ImmersiveModeHandler
import com.axiom.ui.animation.LocalReduceMotion
import com.axiom.api.ui.theme.LocalIsDarkMode
import com.axiom.i18n.ProvideStrings
import com.axiom.i18n.rememberStrings
import com.axiom.ui.theme.AxiomThemeSurface
import com.axiom.ui.widgets.ToastHost
import org.koin.compose.currentKoinScope
import org.koin.compose.koinInject

@Composable
fun AxiomCompositionLocals(content: @Composable BoxScope.() -> Unit) {
    val screenSize = rememberScreenSize()
    val treeSitter = rememberTreeSitter()

    val app: App = currentKoinScope().get()

    val settingsRepository: SettingsRepository = koinInject()
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(initialValue = AppSettings())

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val appTheme by settingsRepository.appTheme.collectAsStateWithLifecycle(initialValue = AppTheme.System)

    val darkMode by remember {
        derivedStateOf {
            when (appTheme) {
                AppTheme.Light -> false
                AppTheme.Dark -> true
                AppTheme.System -> isSystemInDarkTheme
            }
        }
    }

    val editorColorScheme = rememberEditorColorScheme()

    val values by remember {
        derivedStateOf {
            arrayOf(
                LocalScreenSize provides screenSize,
                LocalTreeSitter provides treeSitter,
                LocalIsDarkMode provides darkMode,
                LocalAppSettings provides settings,
                LocalReduceMotion provides settings.appearance.reduceMotion,
                LocalAxiomEditorColorScheme provides editorColorScheme,
                LocalApp provides app,
                LocalEventBus provides app.eventBus()
            )
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val languageRegistry = app.globalOrNull<LanguageRegistry>() as? LanguageRegistryImpl

    DisposableEffect(lifecycleOwner, treeSitter) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                treeSitter.close()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        languageRegistry?.bind(treeSitter)
        onDispose {
            languageRegistry?.unbind()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    CompositionLocalProvider(values = values) {
        ImmersiveModeHandler(
            isImmersiveModeEnabled = settings.appearance.immersiveMode
        ) {
            val languageTag = settings.appearance.effectiveLanguageTag
                ?: Locale.current.toLanguageTag()
            ProvideStrings(rememberStrings(currentLanguageTag = languageTag)) {
                AxiomThemeSurface {
                    content()
                    ToastHost(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
