package com.axiom.lsp.capabilities

import com.axiom.lsp.SymbolTag
import kotlinx.serialization.Serializable

/**
 * @since 3.16.0
 */
@Serializable
data class SymbolTagSupportCapabilities(
    /**
     * The tags supported by the client.
     */
    val valueSet: List<SymbolTag>
)
