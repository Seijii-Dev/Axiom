package com.axiom.data.terminal

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import com.axiom.api.data.terminal.TerminalManager
import com.axiom.api.data.terminal.TerminalSessionManager
import com.axiom.core.unsafe.GlobalApp
import com.axiom.core.unsafe.UnsafeGlobalAccess
import com.axiom.terminal.emulator.TerminalSession
import com.axiom.terminal.ui.BaseTerminalClient
import com.axiom.terminal.ui.extrakeys.ExtraKeysState
import com.axiom.terminal.ui.extrakeys.SpecialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnsafeGlobalAccess::class)
class AxiomTerminalClient(
    private val extraKeysState: ExtraKeysState,
    private val onFinishRequest: () -> Unit = {},
    private val sessionManager: TerminalSessionManager = GlobalApp.global<TerminalManager>().sessionManager,
) : BaseTerminalClient() {

    private val clientScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun readControlKey(): Boolean {
        return extraKeysState.readSpecialButton(SpecialButton.Ctrl) ?: false
    }

    override fun readAltKey(): Boolean {
        return extraKeysState.readSpecialButton(SpecialButton.Alt) ?: false
    }

    override fun readFnKey(): Boolean {
        return extraKeysState.readSpecialButton(SpecialButton.Fn) ?: false
    }

    override fun readShiftKey(): Boolean {
        return extraKeysState.readSpecialButton(SpecialButton.Shift) ?: false
    }

    override fun onKeyDown(key: Key, event: KeyEvent, session: TerminalSession): Boolean {
        if (key == Key.Enter && !session.isRunning.value) {
            finishSession()
            return true
        }
        return super.onKeyDown(key, event, session)
    }

    override fun onCodePoint(codePoint: Int, ctrlDown: Boolean, session: TerminalSession): Boolean {
        // The soft keyboard delivers Enter through the IME as a '\r' commitText (see
        // Terminal.kt's sendTextToTerminal), which never reaches onKeyDown.
        if (codePoint == '\r'.code && !session.isRunning.value) {
            finishSession()
            return true
        }
        return super.onCodePoint(codePoint, ctrlDown, session)
    }

    private fun finishSession() {
        clientScope.launch {
            if (sessionManager.sessions.value.size <= 1) {
                onFinishRequest()
                delay(SESSION_CLOSE_NAVIGATION_DELAY_MILLIS)
            }
            sessionManager.terminateCurrentSession()
        }
    }

    private companion object {
        private const val SESSION_CLOSE_NAVIGATION_DELAY_MILLIS = 400L
    }
}
