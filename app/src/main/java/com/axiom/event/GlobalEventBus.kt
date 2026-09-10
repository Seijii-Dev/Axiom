package com.axiom.event

import com.axiom.core.App
import com.axiom.core.Global
import com.axiom.core.event.EventBus
import com.axiom.core.event.eventBus
import com.axiom.core.unsafe.GlobalApp
import com.axiom.core.unsafe.UnsafeGlobalAccess

private class EventBusGlobal(val bus: EventBus) : Global

@OptIn(UnsafeGlobalAccess::class)
val GlobalEventBus by lazy { GlobalApp.eventBus() }

/**
 * Retrieves the global [EventBus] instance from the [App] registry.
 *
 * @return The registered [EventBus].
 */
fun App.eventBus() = global<EventBusGlobal>().bus

/**
 * Initializes and registers the global [EventBus] into the provided [App] instance.
 * The bus's coroutine lifecycle is tied to the [App.backgroundScope].
 *
 * @param app The application instance where the global bus will be registered.
 */
fun initializeGlobalEventBus(app: App) {
    app.setGlobal(EventBusGlobal(eventBus(app.backgroundScope)))
}
