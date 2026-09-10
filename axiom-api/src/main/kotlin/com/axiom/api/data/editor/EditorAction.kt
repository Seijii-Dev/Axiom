package com.axiom.api.data.editor

import com.axiom.api.data.file.KxFile

sealed interface EditorAction

data class Save(val file: KxFile) : EditorAction
data class SaveAs(val oldTabId: String, val newFile: KxFile) : EditorAction
