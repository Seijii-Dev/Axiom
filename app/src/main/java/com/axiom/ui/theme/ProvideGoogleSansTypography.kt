package com.axiom.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.axiom.api.ui.theme.GoogleSansTypography

@Composable
fun ProvideGoogleSansTypography(content: @Composable () -> Unit) {
    // Google Sans Rounded has no Devanagari glyphs; render those locales in Laila.
    val typography = if (isDevanagariLocale()) {
        GoogleSansTypography.withLailaFont()
    } else {
        GoogleSansTypography
    }

    MaterialTheme(
        typography = typography,
        content = content
    )
}
