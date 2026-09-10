package com.axiom.lsp

import com.axiom.lsp.types.LSPAny
import kotlinx.serialization.Serializable

/**
 * [LSP Specification](https://microsoft.github.io/language-server-protocol/specifications/lsp/3.18/specification/#command)
 */
@Serializable
data class Command(
    /**
     * Title of the command, like `save`.
     */
    val title: String,

    /**
     * The identifier of the actual command handler.
     */
    val command: String,

    /**
     * An optional tooltip.
     */
    var tooltip: String? = null,

    /**
     * Arguments that the command handler should be
     * invoked with.
     */
    var arguments: LSPAny? = null
)
