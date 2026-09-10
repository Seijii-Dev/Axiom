package com.axiom.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axiom.api.data.preferences.AppSettings
import com.axiom.api.data.preferences.AppTheme
import com.axiom.api.data.preferences.EditorSettings
import com.axiom.data.preferences.SettingsRepository
import com.axiom.api.util.stateInWhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val settings = settingsRepository.settings
        .stateInWhileSubscribed(initialValue = AppSettings())

    val appearanceSettings = settingsRepository.appearanceSettings
        .stateInWhileSubscribed(initialValue = settings.value.appearance)

    val editorSettings = settingsRepository.settings
        .map { it.editor }
        .stateInWhileSubscribed(initialValue = EditorSettings())

    fun updateAppTheme(newTheme: AppTheme) {
        viewModelScope.launch {
            settingsRepository.updateAppTheme(newTheme)
        }
    }
}
