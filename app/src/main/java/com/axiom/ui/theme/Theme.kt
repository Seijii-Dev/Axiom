package com.axiom.ui.theme

import android.os.Build
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.axiom.api.data.preferences.LocalAppSettings
import com.axiom.api.ui.theme.LocalIsDarkMode
import com.axiom.api.ui.theme.GoogleSansTypography
import com.axiom.api.ui.theme.Typography
import com.axiom.api.ui.theme.backgroundDark
import com.axiom.api.ui.theme.backgroundLight
import com.axiom.api.ui.theme.errorContainerDark
import com.axiom.api.ui.theme.errorContainerLight
import com.axiom.api.ui.theme.errorDark
import com.axiom.api.ui.theme.errorLight
import com.axiom.api.ui.theme.inverseOnSurfaceDark
import com.axiom.api.ui.theme.inverseOnSurfaceLight
import com.axiom.api.ui.theme.inversePrimaryDark
import com.axiom.api.ui.theme.inversePrimaryLight
import com.axiom.api.ui.theme.inverseSurfaceDark
import com.axiom.api.ui.theme.inverseSurfaceLight
import com.axiom.api.ui.theme.onBackgroundDark
import com.axiom.api.ui.theme.onBackgroundLight
import com.axiom.api.ui.theme.onErrorContainerDark
import com.axiom.api.ui.theme.onErrorContainerLight
import com.axiom.api.ui.theme.onErrorDark
import com.axiom.api.ui.theme.onErrorLight
import com.axiom.api.ui.theme.onPrimaryContainerDark
import com.axiom.api.ui.theme.onPrimaryContainerLight
import com.axiom.api.ui.theme.onPrimaryDark
import com.axiom.api.ui.theme.onPrimaryLight
import com.axiom.api.ui.theme.onSecondaryContainerDark
import com.axiom.api.ui.theme.onSecondaryContainerLight
import com.axiom.api.ui.theme.onSecondaryDark
import com.axiom.api.ui.theme.onSecondaryLight
import com.axiom.api.ui.theme.onSurfaceDark
import com.axiom.api.ui.theme.onSurfaceLight
import com.axiom.api.ui.theme.onSurfaceVariantDark
import com.axiom.api.ui.theme.onSurfaceVariantLight
import com.axiom.api.ui.theme.onTertiaryContainerDark
import com.axiom.api.ui.theme.onTertiaryContainerLight
import com.axiom.api.ui.theme.onTertiaryDark
import com.axiom.api.ui.theme.onTertiaryLight
import com.axiom.api.ui.theme.outlineDark
import com.axiom.api.ui.theme.outlineLight
import com.axiom.api.ui.theme.outlineVariantDark
import com.axiom.api.ui.theme.outlineVariantLight
import com.axiom.api.ui.theme.primaryContainerDark
import com.axiom.api.ui.theme.primaryContainerLight
import com.axiom.api.ui.theme.primaryDark
import com.axiom.api.ui.theme.primaryLight
import com.axiom.api.ui.theme.scrimDark
import com.axiom.api.ui.theme.scrimLight
import com.axiom.api.ui.theme.secondaryContainerDark
import com.axiom.api.ui.theme.secondaryContainerLight
import com.axiom.api.ui.theme.secondaryDark
import com.axiom.api.ui.theme.secondaryLight
import com.axiom.api.ui.theme.surfaceBrightDark
import com.axiom.api.ui.theme.surfaceBrightLight
import com.axiom.api.ui.theme.surfaceContainerDark
import com.axiom.api.ui.theme.surfaceContainerHighDark
import com.axiom.api.ui.theme.surfaceContainerHighLight
import com.axiom.api.ui.theme.surfaceContainerHighestDark
import com.axiom.api.ui.theme.surfaceContainerHighestLight
import com.axiom.api.ui.theme.surfaceContainerLight
import com.axiom.api.ui.theme.surfaceContainerLowDark
import com.axiom.api.ui.theme.surfaceContainerLowLight
import com.axiom.api.ui.theme.surfaceContainerLowestDark
import com.axiom.api.ui.theme.surfaceContainerLowestLight
import com.axiom.api.ui.theme.surfaceDark
import com.axiom.api.ui.theme.surfaceDimDark
import com.axiom.api.ui.theme.surfaceDimLight
import com.axiom.api.ui.theme.surfaceLight
import com.axiom.api.ui.theme.surfaceVariantDark
import com.axiom.api.ui.theme.surfaceVariantLight
import com.axiom.api.ui.theme.tertiaryContainerDark
import com.axiom.api.ui.theme.tertiaryContainerLight
import com.axiom.api.ui.theme.tertiaryDark
import com.axiom.api.ui.theme.tertiaryLight
import com.axiom.ui.animation.LocalReduceMotion
import com.axiom.ui.animation.orSnap

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private fun ColorScheme.applyAmoled(): ColorScheme {
    return this.copy(
        background = Color.Black,
        surface = Color.Black,
        surfaceContainerLowest = Color.Black,
        surfaceContainerLow = Color(0xFF0A0A0A),
        surfaceContainer = Color(0xFF121212)
    )
}

@Composable
fun AxiomTheme(
    darkTheme: Boolean = LocalIsDarkMode.current,
    amoled: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    val reduceMotion = LocalReduceMotion.current
    val finalColorScheme = if (darkTheme && amoled) colorScheme.applyAmoled() else colorScheme

    // Google Sans Rounded has no Devanagari glyphs; render those locales in Laila.
    val typography = if (isDevanagariLocale()) {
        GoogleSansTypography.withLailaFont()
    } else {
        Typography
    }

    MaterialExpressiveTheme(
        colorScheme = finalColorScheme,
        typography = typography,
        motionScheme = reducedMotionScheme(reduceMotion),
        content = content
    )
}

private fun reducedMotionScheme(reduceMotion: Boolean) = object : MotionScheme {

    val expressive = MotionScheme.expressive()

    override fun <T> defaultSpatialSpec(): FiniteAnimationSpec<T> {
        return expressive.defaultSpatialSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> fastSpatialSpec(): FiniteAnimationSpec<T> {
        return expressive.fastSpatialSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> slowSpatialSpec(): FiniteAnimationSpec<T> {
        return expressive.slowSpatialSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> defaultEffectsSpec(): FiniteAnimationSpec<T> {
        return expressive.defaultEffectsSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> fastEffectsSpec(): FiniteAnimationSpec<T> {
        return expressive.fastEffectsSpec<T>().orSnap(reduceMotion)
    }

    override fun <T> slowEffectsSpec(): FiniteAnimationSpec<T> {
        return expressive.slowEffectsSpec<T>().orSnap(reduceMotion)
    }
}

@Composable
fun AxiomThemeSurface(content: @Composable BoxScope.() -> Unit) {
    AxiomTheme(amoled = LocalAppSettings.current.appearance.amoledDarkMode) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    content = content
                )
            }
        )
    }
}
