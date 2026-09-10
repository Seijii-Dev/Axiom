package com.axiom.lsp.capabilities

import com.axiom.lsp.CodeActionTag
import kotlinx.serialization.Serializable

@Serializable
data class CodeActionTagSupportClientCapabilities(
    /**
     * The tags supported by the client.
     */
    val valueSet: List<CodeActionTag>
)
