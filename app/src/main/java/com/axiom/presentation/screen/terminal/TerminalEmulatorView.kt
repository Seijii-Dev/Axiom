package com.axiom.presentation.screen.terminal

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.axiom.api.data.preferences.TerminalSettings
import com.axiom.api.data.terminal.TerminalManager
import com.axiom.api.ui.theme.JetBrainsMonoFontFamily
import com.axiom.api.ui.theme.LocalIsDarkMode
import com.axiom.core.globalOf
import com.axiom.data.terminal.ExtraTerminalKeys
import com.axiom.data.terminal.AxiomExtraKeysClient
import com.axiom.data.terminal.AxiomTerminalClient
import com.axiom.data.terminal.AxiomTerminalTheme
import com.axiom.presentation.navigation.Navigator
import com.axiom.terminal.ui.Terminal
import com.axiom.terminal.ui.extrakeys.ExtraKeyStyle
import com.axiom.terminal.ui.extrakeys.ExtraKeys
import com.axiom.terminal.ui.extrakeys.ExtraKeysConstants
import com.axiom.terminal.ui.extrakeys.ExtraKeysInfo
import com.axiom.terminal.ui.extrakeys.rememberExtraKeysState
import com.axiom.terminal.ui.rememberTerminalSessionClient
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val json = Json { prettyPrint = true; encodeDefaults = true; explicitNulls = false }

@SuppressLint("ComposableNaming")
@Composable
fun applyTerminalTheme() {
    val isDark = LocalIsDarkMode.current
    val surfaceColor = MaterialTheme.colorScheme.surface
    LaunchedEffect(isDark, surfaceColor) {
        AxiomTerminalTheme.apply(isDark, surfaceColor)
    }
}

@Composable
fun TerminalEmulator(
    isServiceBound: Boolean,
    navigator: Navigator,
    onTitleChange: (String?) -> Unit,
    terminalSettings: TerminalSettings
) {
    applyTerminalTheme()

    if (!isServiceBound) {
        TerminalServiceBindingIndicator()
        return
    }

    val sessionManager = globalOf<TerminalManager>().sessionManager
    val sessions by sessionManager.sessions.collectAsStateWithLifecycle()
    val currentEntry by sessionManager.currentSession.collectAsStateWithLifecycle()

    val sessionClient = rememberTerminalSessionClient(
        onTitleChanged = { onTitleChange(it.title) },
        cursorStyle = terminalSettings.cursorStyle,
        bellEnabled = terminalSettings.bellEnabled,
        bellVolume = terminalSettings.bellVolume,
        bellSoundType = terminalSettings.bellSoundType
    )

    LaunchedEffect(terminalSettings.cursorStyle, sessions) {
        sessions.forEach { entry ->
            entry.session.updateTerminalSessionClient(sessionClient)
        }
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (sessions.isEmpty()) {
            sessionManager.newSession(
                client = sessionClient,
                transcriptRows = terminalSettings.scrollbackLines,
                showMotd = terminalSettings.showMotd
            )
        }
    }

    LaunchedEffect(currentEntry?.id) {
        onTitleChange(currentEntry?.session?.title)
    }

    val session = currentEntry?.session

    if (session != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            TerminalSessionTabs(
                sessions = sessions,
                activeSessionId = currentEntry?.id,
                onSelectSession = { id -> sessionManager.switchTo(id) },
                onCloseSession = { id -> scope.launch { sessionManager.terminate(id) } },
                onNewSession = {
                    scope.launch {
                        sessionManager.newSession(
                            client = sessionClient,
                            transcriptRows = terminalSettings.scrollbackLines,
                            showMotd = terminalSettings.showMotd
                        )
                    }
                }
            )

            val extraKeysClient = remember(session) { AxiomExtraKeysClient(session) }
            val extraKeysState = rememberExtraKeysState()

            val terminalClient = remember {
                AxiomTerminalClient(
                    extraKeysState = extraKeysState,
                    onFinishRequest = { navigator.navigateBack() }
                )
            }

            key(currentEntry?.id) {
                Terminal(
                    modifier = Modifier.weight(1f),
                    session = session,
                    fontFamily = JetBrainsMonoFontFamily,
                    fontSize = terminalSettings.fontSize.sp,
                    client = terminalClient,
                    cursorBlink = terminalSettings.cursorBlink
                )
            }

            if (terminalSettings.extraKeysStyle != None) {
                ExtraKeys(
                    extraKeysInfo = ExtraKeysInfo(
                        propertiesInfo = json.encodeToString(ExtraTerminalKeys),
                        style = terminalSettings.extraKeysStyle,
                        extraKeyAliasMap = ExtraKeysConstants.CONTROL_CHARS_ALIASES
                    ),
                    state = extraKeysState,
                    client = extraKeysClient,
                    modifier = Modifier.height(75.dp)
                )
            }
        }
    } else {
        TerminalSessionLoading()
    }
}
