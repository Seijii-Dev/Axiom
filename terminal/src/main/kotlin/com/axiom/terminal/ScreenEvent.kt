package com.axiom.terminal

sealed interface ScreenEvent {
    data class ContentChanged(val skipScrolling: Boolean) : ScreenEvent
    data class CursorBlink(val visible: Boolean) : ScreenEvent
}
