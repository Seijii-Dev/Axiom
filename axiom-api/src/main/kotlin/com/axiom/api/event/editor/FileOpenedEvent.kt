package com.axiom.api.event.editor

import android.net.Uri
import com.axiom.api.data.editor.FileOpener
import com.axiom.api.data.editor.WorkspaceTab
import com.axiom.api.event.eventBus
import com.axiom.core.event.EventBus

/**
 * Published on the application [EventBus] whenever a file is opened in
 * the workspace, regardless of whether it was opened by Axiom natively or
 * by a plugin [FileOpener].
 *
 * Plugins can subscribe to react to file opens (e.g. start an analysis) via [eventBus].
 *
 * @property uri The URI of the opened file.
 * @property fileName The display name of the file.
 * @property tabId The id of the [WorkspaceTab] created for it.
 * @property projectUri The project the file belongs to, if any.
 */
data class FileOpenedEvent(
    val uri: Uri,
    val fileName: String,
    val tabId: String,
    val projectUri: Uri? = null
)
