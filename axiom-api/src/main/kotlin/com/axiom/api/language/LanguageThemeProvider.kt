package com.axiom.api.language

fun interface LanguageThemeProvider {
    fun getStyleForCapture(captureName: String): CaptureStyle?
}
