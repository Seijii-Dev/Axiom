package com.axiom.lsp

import android.os.Process
import com.axiom.BuildConfig
import com.axiom.lsp.capabilities.ClientCapabilities
import com.axiom.lsp.capabilities.CodeActionCapabilities
import com.axiom.lsp.capabilities.CodeActionKindCapabilities
import com.axiom.lsp.capabilities.CodeActionLiteralSupportCapabilities
import com.axiom.lsp.capabilities.CompletionCapabilities
import com.axiom.lsp.capabilities.CompletionItemCapabilities
import com.axiom.lsp.capabilities.DefinitionCapabilities
import com.axiom.lsp.capabilities.DiagnosticClientCapabilities
import com.axiom.lsp.capabilities.DiagnosticWorkspaceClientCapabilities
import com.axiom.lsp.capabilities.DidChangeConfigurationCapabilities
import com.axiom.lsp.capabilities.DidChangeWatchedFilesCapabilities
import com.axiom.lsp.capabilities.DocumentSymbolCapabilities
import com.axiom.lsp.capabilities.ExecuteCommandCapabilities
import com.axiom.lsp.capabilities.FormattingCapabilities
import com.axiom.lsp.capabilities.HoverCapabilities
import com.axiom.lsp.capabilities.InlayHintClientCapabilities
import com.axiom.lsp.capabilities.MessageActionItemCapabilities
import com.axiom.lsp.capabilities.OnTypeFormattingCapabilities
import com.axiom.lsp.capabilities.ParameterInformationCapabilities
import com.axiom.lsp.capabilities.PublishDiagnosticsCapabilities
import com.axiom.lsp.capabilities.RangeFormattingCapabilities
import com.axiom.lsp.capabilities.RenameCapabilities
import com.axiom.lsp.capabilities.ShowDocumentCapabilities
import com.axiom.lsp.capabilities.ShowMessageRequestCapabilities
import com.axiom.lsp.capabilities.SignatureHelpCapabilities
import com.axiom.lsp.capabilities.SignatureInformationCapabilities
import com.axiom.lsp.capabilities.SymbolKindCapabilities
import com.axiom.lsp.capabilities.SynchronizationCapabilities
import com.axiom.lsp.capabilities.TextDocumentClientCapabilities
import com.axiom.lsp.capabilities.WindowClientCapabilities
import com.axiom.lsp.capabilities.WorkspaceClientCapabilities
import com.axiom.lsp.capabilities.WorkspaceEditCapabilities
import com.axiom.lsp.capabilities.WorkspaceSymbolCapabilities
import com.axiom.lsp.types.LSPAny
import java.io.File

internal fun createInitializeParams(
    project: File?,
    initializationOptions: LSPAny? = null
): InitializeParams {
    return InitializeParams(
        processId = Process.myPid(),
        clientInfo = ClientInfo("Axiom", BuildConfig.VERSION_NAME),
        capabilities = ClientCapabilities(
            textDocument = TextDocumentClientCapabilities(
                synchronization = SynchronizationCapabilities(
                    dynamicRegistration = true,
                    willSave = true,
                    willSaveWaitUntil = true,
                    didSave = true
                ),
                codeAction = CodeActionCapabilities(
                    dynamicRegistration = true,
                    isPreferredSupport = true,
                    codeActionLiteralSupport = CodeActionLiteralSupportCapabilities(
                        codeActionKind = CodeActionKindCapabilities(
                            listOf(
                                CodeActionKind.QuickFix,
                                CodeActionKind.Refactor,
                                CodeActionKind.RefactorInline,
                                CodeActionKind.RefactorExtract,
                                CodeActionKind.RefactorRewrite,
                                CodeActionKind.Source,
                                CodeActionKind.SourceOrganizeImports,
                                CodeActionKind.SourceFixAll
                            )
                        )
                    )
                ),
                completion = CompletionCapabilities(
                    dynamicRegistration = true,
                    completionItem = CompletionItemCapabilities(
                        snippetSupport = true,
                        commitCharactersSupport = true,
                        documentationFormat = listOf(MarkupKind.Markdown, MarkupKind.PlainText),
                        deprecatedSupport = true,
                        preselectSupport = true
                    )
                ),
                hover = HoverCapabilities(
                    dynamicRegistration = true,
                    contentFormat = listOf(MarkupKind.Markdown, MarkupKind.PlainText)
                ),
                signatureHelp = SignatureHelpCapabilities(
                    dynamicRegistration = true,
                    signatureInformation = SignatureInformationCapabilities(
                        documentationFormat = listOf(MarkupKind.Markdown, MarkupKind.PlainText),
                        parameterInformation = ParameterInformationCapabilities(labelOffsetSupport = true)
                    ),
                    contextSupport = true
                ),
                definition = DefinitionCapabilities(dynamicRegistration = true),
                documentSymbol = DocumentSymbolCapabilities(
                    dynamicRegistration = true,
                    symbolKind = SymbolKindCapabilities(valueSet = SymbolKind.entries)
                ),
                formatting = FormattingCapabilities(dynamicRegistration = true),
                rangeFormatting = RangeFormattingCapabilities(dynamicRegistration = true),
                onTypeFormatting = OnTypeFormattingCapabilities(dynamicRegistration = true),
                rename = RenameCapabilities(
                    dynamicRegistration = true,
                    prepareSupport = true
                ),
                publishDiagnostics = PublishDiagnosticsCapabilities(
                    relatedInformation = true,
                    versionSupport = true
                ),
                inlayHint = InlayHintClientCapabilities(dynamicRegistration = true),
                diagnostic = DiagnosticClientCapabilities(
                    relatedDocumentSupport = true,
                    relatedInformation = true
                )
            ),
            workspace = WorkspaceClientCapabilities(
                applyEdit = true,
                workspaceEdit = WorkspaceEditCapabilities(
                    documentChanges = true,
                    resourceOperations = listOf(
                        ResourceOperationKind.Create,
                        ResourceOperationKind.Rename,
                        ResourceOperationKind.Delete
                    ),
                    failureHandling = FailureHandlingKind.TextOnlyTransactional
                ),
                didChangeConfiguration = DidChangeConfigurationCapabilities(dynamicRegistration = true),
                didChangeWatchedFiles = DidChangeWatchedFilesCapabilities(dynamicRegistration = true),
                symbol = WorkspaceSymbolCapabilities(
                    dynamicRegistration = true,
                    symbolKind = SymbolKindCapabilities(valueSet = SymbolKind.entries)
                ),
                executeCommand = ExecuteCommandCapabilities(dynamicRegistration = true),
                diagnostics = DiagnosticWorkspaceClientCapabilities(refreshSupport = true)
            ),
            window = WindowClientCapabilities(
                showMessage = ShowMessageRequestCapabilities(
                    messageActionItem = MessageActionItemCapabilities(additionalPropertiesSupport = true)
                ),
                showDocument = ShowDocumentCapabilities(support = true),
                workDoneProgress = true
            )
        ),
        initializationOptions = initializationOptions
    ).apply {
        if (project != null) {
            @Suppress("DEPRECATION")
            rootUri = project.toLspUri()
            workspaceFolders = listOf(
                WorkspaceFolder(
                    uri = project.toLspUri(),
                    name = project.name
                )
            )
        }
    }
}
