package com.axiom.ui.provider

import androidx.compose.runtime.staticCompositionLocalOf
import com.axiom.core.event.EventBus

val LocalEventBus = staticCompositionLocalOf<EventBus> {
    error("No LocalEventBus provided")
}
