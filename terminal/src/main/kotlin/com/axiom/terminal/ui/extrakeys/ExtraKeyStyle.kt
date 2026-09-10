package com.axiom.terminal.ui.extrakeys

import kotlinx.serialization.Serializable

@Serializable
enum class ExtraKeyStyle {
    ArrowsOnly,
    ArrowsAll,
    All,
    None,
    Default
}
