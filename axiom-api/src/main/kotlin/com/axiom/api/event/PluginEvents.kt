package com.axiom.api.event

import com.axiom.api.InternalAxiomApi
import com.axiom.api.event.editor.FileOpenedEvent
import com.axiom.api.plugin.PluginContext
import com.axiom.core.Global
import com.axiom.core.event.EventBus

/**
 * A [Global] wrapper that exposes the application's [EventBus] to plugins.
 *
 * Registered by the host during startup; plugins reach the bus through [PluginContext.eventBus].
 */
@InternalAxiomApi
class EventBusHolder(val bus: EventBus) : Global

/**
 * The application-wide [EventBus].
 *
 * Plugins can publish their own events and subscribe to built-in ones (such as
 * [FileOpenedEvent]) through this bus.
 *
 * ### Example
 * ```kotlin
 * suspend fun watchOpens() {
 *     currentPluginContext().eventBus.subscribe<FileOpenedEvent> { event ->
 *         println("Opened ${event.fileName}")
 *     }
 * }
 * ```
 */
@OptIn(InternalAxiomApi::class)
val PluginContext.eventBus: EventBus
    get() = app.global<EventBusHolder>().bus
