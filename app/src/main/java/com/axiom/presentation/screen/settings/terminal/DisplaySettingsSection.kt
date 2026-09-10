package com.axiom.presentation.screen.settings.terminal

import androidx.compose.runtime.Composable
import com.axiom.api.data.preferences.TerminalSettings as TerminalSettingsPrefs
import com.axiom.data.preferences.updateTerminalSettings
import com.axiom.i18n.strings
import com.axiom.presentation.screen.settings.components.SettingsSubsection
import com.axiom.presentation.screen.settings.components.SliderSettingsItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DisplaySettingsSection(
    settings: TerminalSettingsPrefs,
    scope: CoroutineScope
) {
    SettingsSubsection(title = strings.displaySection) {
        SliderSettingsItem(
            label = strings.scrollbackLines,
            value = settings.scrollbackLines.toFloat(),
            valueRange = 100f..50000f,
            steps = 0,
            onValueChange = { lines ->
                scope.launch {
                    updateTerminalSettings { copy(scrollbackLines = lines.toInt()) }
                }
            },
            valueText = { "${it.toInt()}" }
        )
    }
}
