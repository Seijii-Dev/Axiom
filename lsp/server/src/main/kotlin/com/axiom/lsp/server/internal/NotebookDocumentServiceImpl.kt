package com.axiom.lsp.server.internal

import com.axiom.lsp.DidChangeNotebookDocumentParams
import com.axiom.lsp.DidCloseNotebookDocumentParams
import com.axiom.lsp.DidOpenNotebookDocumentParams
import com.axiom.lsp.DidSaveNotebookDocumentParams
import com.axiom.lsp.server.NotebookDocumentService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

internal class NotebookDocumentServiceImpl(
    val connection: JsonRpcConnection,
    val json: Json
) : NotebookDocumentService {

    private suspend inline fun <reified Params> sendNotification(method: String, params: Params? = null) {
        connection.sendNotification("notebookDocument/$method", json.encodeToJsonElement(params))
    }

    override suspend fun didOpen(params: DidOpenNotebookDocumentParams) {
        sendNotification("didOpen", params)
    }

    override suspend fun didChange(params: DidChangeNotebookDocumentParams) {
        sendNotification("didChange", params)
    }

    override suspend fun didSave(params: DidSaveNotebookDocumentParams) {
        sendNotification("didSave", params)
    }

    override suspend fun didClose(params: DidCloseNotebookDocumentParams) {
        sendNotification("didClose", params)
    }
}
