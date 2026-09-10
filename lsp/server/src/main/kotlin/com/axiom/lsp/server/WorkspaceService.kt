@file:Suppress("DEPRECATION")

package com.axiom.lsp.server

import com.axiom.lsp.CreateFilesParams
import com.axiom.lsp.DeleteFilesParams
import com.axiom.lsp.DidChangeConfigurationParams
import com.axiom.lsp.DidChangeWatchedFilesParams
import com.axiom.lsp.DidChangeWatchedFilesRegistrationOptions
import com.axiom.lsp.DidChangeWorkspaceFoldersParams
import com.axiom.lsp.ExecuteCommandParams
import com.axiom.lsp.RenameFilesParams
import com.axiom.lsp.SymbolInformation
import com.axiom.lsp.TextDocumentContentParams
import com.axiom.lsp.TextDocumentContentRegistrationOptions
import com.axiom.lsp.TextDocumentContentResult
import com.axiom.lsp.WorkspaceDiagnosticParams
import com.axiom.lsp.WorkspaceDiagnosticReport
import com.axiom.lsp.WorkspaceEdit
import com.axiom.lsp.WorkspaceSymbol
import com.axiom.lsp.WorkspaceSymbolParams
import com.axiom.lsp.types.LSPAny
import com.axiom.lsp.types.OneOf

interface WorkspaceService {
    /**
     * The workspace diagnostic request is sent from the client to the server to ask the
     * server to compute workspace wide diagnostics which previously were pushed from the
     * server to the client. In contrast to the document diagnostic request, the workspace
     * request can be long running and is not bound to a specific workspace or document state.
     * If the client supports streaming for the workspace diagnostic pull, it is legal to provide a
     * document diagnostic report multiple times for the same document URI. The last one reported will
     * win over previous reports.
     *
     * If a client receives a diagnostic report for a document in a workspace diagnostic request
     * for which the client also issues individual document diagnostic pull requests, the client
     * needs to decide which diagnostics win and should be presented. In general:
     *
     * - diagnostics for a higher document version should win over those from a lower document
     *   version (e.g. note that document versions are steadily increasing)
     * - diagnostics from a document pull should win over diagnostics from a workspace pull.
     *
     * @since 3.17.0
     */
    suspend fun diagnostic(params: WorkspaceDiagnosticParams): WorkspaceDiagnosticReport

    /**
     * The workspace symbol request is sent from the client to the server to list project-wide
     * symbols matching the query string.
     *
     * Registration Options: [WorkspaceSymbolRegistrationOptions][com.axiom.lsp.WorkspaceSymbolRegistrationOptions]
     */
    suspend fun symbol(params: WorkspaceSymbolParams): OneOf<List<SymbolInformation>, List<WorkspaceSymbol>>?

    /**
     * The request is sent from the client to the server to resolve additional
     * information for a given workspace symbol.
     *
     * @since 3.17.0
     */
    suspend fun resolveWorkspaceSymbol(symbol: WorkspaceSymbol): WorkspaceSymbol

    /**
     * A notification sent from the client to the server to signal the change of configuration settings.
     */
    suspend fun didChangeConfiguration(params: DidChangeConfigurationParams)

    /**
     * The `workspace/didChangeWorkspaceFolders` notification is sent from the client
     * to the server to inform the server about workspace folder configuration changes.
     * The notification is sent by default if both ServerCapabilities/workspaceFolders
     * and ClientCapabilities/workspace/workspaceFolders are true; or if the server has
     * registered to receive this notification it first.
     *
     * @since 3.6.0
     */
    suspend fun didChangeWorkspaceFolders(params: DidChangeWorkspaceFoldersParams)

    /**
     * The will create files request is sent from the client to the server before files are actually
     * created as long as the creation is triggered from within the client either by a user action
     * or by applying a workspace edit. The request can return a [WorkspaceEdit] which will be applied to
     * the workspace before the files are created. Hence, the [WorkspaceEdit] cannot manipulate the content
     * of the files to be created. Please note that clients might drop results if computing the edit took
     * too long or if a server constantly fails on this request. This is done to keep creates fast and reliable.
     *
     * @since 3.16.0
     */
    suspend fun willCreateFiles(params: CreateFilesParams): WorkspaceEdit?

    /**
     * The did create files notification is sent from the client to the server when files
     * were created from within the client.
     *
     * @since 3.16.0
     */
    suspend fun didCreateFiles(params: CreateFilesParams)

    /**
     * The will rename files request is sent from the client to the server before files are actually
     * renamed as long as the rename is triggered from within the client either by a user action or by
     * applying a workspace edit. The request can return a [WorkspaceEdit] which will be applied to the
     * workspace before the files are renamed. Please note that clients might drop results if computing
     * the edit took too long or if a server constantly fails on this request. This is done to keep renames
     * fast and reliable.
     *
     * @since 3.16.0
     */
    suspend fun willRenameFiles(params: RenameFilesParams): WorkspaceEdit?

    /**
     * The did rename files notification is sent from the client to the server when files
     * were renamed from within the client.
     *
     * @since 3.16.0
     */
    suspend fun didRenameFiles(params: RenameFilesParams)

    /**
     * The will delete files request is sent from the client to the server before files are actually
     * deleted as long as the deletion is triggered from within the client either by a user action or
     * by applying a workspace edit. The request can return a [WorkspaceEdit] which will be applied to
     * the workspace before the files are deleted. Please note that clients might drop results if computing
     * the edit took too long or if a server constantly fails on this request. This is done to keep deletes
     * fast and reliable.
     *
     * @since 3.16.0
     */
    suspend fun willDeleteFiles(params: DeleteFilesParams): WorkspaceEdit?

    /**
     * The did delete files notification is sent from the client to the server when files
     * were deleted from within the client.
     *
     * @since 3.16.0
     */
    suspend fun didDeleteFiles(params: DeleteFilesParams)

    /**
     * The watched files notification is sent from the client to the server when the client detects
     * changes to files and folders watched by the language client (note although the name suggest that
     * only file events are sent, it is about file system events which include folders as well).
     *
     * Registration Options: [DidChangeWatchedFilesRegistrationOptions]
     */
    suspend fun didChangeWatchedFiles(params: DidChangeWatchedFilesParams)

    /**
     * The `workspace/executeCommand` request is sent from the client to the server to trigger
     * command execution on the server. In most cases, the server creates a [WorkspaceEdit] structure
     * and applies the changes to the workspace using the request [`workspace/applyEdit`][LanguageClient.applyEdit],
     * which is sent from the server to the client.
     */
    suspend fun executeCommand(params: ExecuteCommandParams): LSPAny

    /**
     * The `workspace/textDocumentContent` request is sent from the client to the server to dynamically fetch
     * the content of a text document. Clients should treat the content returned from this requests as readonly.
     *
     * Registration Options: [TextDocumentContentRegistrationOptions]
     *
     * @since 3.18.0
     */
    suspend fun textDocumentContent(params: TextDocumentContentParams): TextDocumentContentResult
}
