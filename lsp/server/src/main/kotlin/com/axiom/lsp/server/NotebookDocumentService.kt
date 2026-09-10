package com.axiom.lsp.server

import com.axiom.lsp.DidChangeNotebookDocumentParams
import com.axiom.lsp.DidCloseNotebookDocumentParams
import com.axiom.lsp.DidOpenNotebookDocumentParams
import com.axiom.lsp.DidSaveNotebookDocumentParams

/**
 * @since 3.17.0
 */
interface NotebookDocumentService {
    /**
     * The open notification is sent from the client to the server
     * when a notebook document is opened. It is only sent by a client
     * if the server requested the synchronization mode `notebook` in
     * its `notebookDocumentSync` capability.
     *
     * @since 3.17.0
     */
    suspend fun didOpen(params: DidOpenNotebookDocumentParams)

    /**
     * The change notification is sent from the client to the server when
     * a notebook document changes. It is only sent by a client if the server
     * requested the synchronization mode `notebook` in its `notebookDocumentSync` capability.
     *
     * @since 3.17.0
     */
    suspend fun didChange(params: DidChangeNotebookDocumentParams)

    /**
     * The save notification is sent from the client to the server when a
     * notebook document is saved. It is only sent by a client if the
     * server requested the synchronization mode `notebook` in
     * its `notebookDocumentSync` capability.
     *
     * @since 3.17.0
     */
    suspend fun didSave(params: DidSaveNotebookDocumentParams)

    /**
     * The close notification is sent from the client to the server when a
     * notebook document is closed. It is only sent by a client if the server
     * requested the synchronization mode `notebook` in its
     * `notebookDocumentSync` capability.
     *
     * @since 3.17.0
     */
    suspend fun didClose(params: DidCloseNotebookDocumentParams)
}
