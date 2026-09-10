package com.axiom.lsp.capabilities

import com.axiom.lsp.CallHierarchyOptions
import com.axiom.lsp.CallHierarchyRegistrationOptions
import com.axiom.lsp.CodeActionOptions
import com.axiom.lsp.CodeLensOptions
import com.axiom.lsp.CompletionOptions
import com.axiom.lsp.DeclarationOptions
import com.axiom.lsp.DeclarationRegistrationOptions
import com.axiom.lsp.DefinitionOptions
import com.axiom.lsp.DiagnosticOptions
import com.axiom.lsp.DiagnosticRegistrationOptions
import com.axiom.lsp.DocumentColorOptions
import com.axiom.lsp.DocumentColorRegistrationOptions
import com.axiom.lsp.DocumentFormattingOptions
import com.axiom.lsp.DocumentHighlightOptions
import com.axiom.lsp.DocumentLinkOptions
import com.axiom.lsp.DocumentOnTypeFormattingOptions
import com.axiom.lsp.DocumentRangeFormattingOptions
import com.axiom.lsp.DocumentSymbolOptions
import com.axiom.lsp.ExecuteCommandOptions
import com.axiom.lsp.FoldingRangeOptions
import com.axiom.lsp.FoldingRangeRegistrationOptions
import com.axiom.lsp.HoverOptions
import com.axiom.lsp.ImplementationOptions
import com.axiom.lsp.ImplementationRegistrationOptions
import com.axiom.lsp.InlayHintOptions
import com.axiom.lsp.InlayHintRegistrationOptions
import com.axiom.lsp.InlineCompletionOptions
import com.axiom.lsp.InlineValueOptions
import com.axiom.lsp.InlineValueRegistrationOptions
import com.axiom.lsp.LinkedEditingRangeOptions
import com.axiom.lsp.LinkedEditingRangeRegistrationOptions
import com.axiom.lsp.MonikerOptions
import com.axiom.lsp.MonikerRegistrationOptions
import com.axiom.lsp.NotebookDocumentSyncOptions
import com.axiom.lsp.NotebookDocumentSyncRegistrationOptions
import com.axiom.lsp.PositionEncodingKind
import com.axiom.lsp.ReferenceOptions
import com.axiom.lsp.RenameOptions
import com.axiom.lsp.SelectionRangeOptions
import com.axiom.lsp.SelectionRangeRegistrationOptions
import com.axiom.lsp.SemanticTokensOptions
import com.axiom.lsp.SemanticTokensRegistrationOptions
import com.axiom.lsp.SignatureHelpOptions
import com.axiom.lsp.TextDocumentSyncKind
import com.axiom.lsp.TextDocumentSyncOptions
import com.axiom.lsp.TypeDefinitionOptions
import com.axiom.lsp.TypeDefinitionRegistrationOptions
import com.axiom.lsp.TypeHierarchyOptions
import com.axiom.lsp.TypeHierarchyRegistrationOptions
import com.axiom.lsp.WorkspaceSymbolOptions
import com.axiom.lsp.types.LSPAny
import com.axiom.lsp.types.OneOf
import com.axiom.lsp.types.OneOfThree
import kotlinx.serialization.Serializable

/**
 * [LSP Specification](https://microsoft.github.io/language-server-protocol/specifications/lsp/3.18/specification/#serverCapabilities)
 */
@Serializable
data class ServerCapabilities(
    /**
     * The position encoding the server picked from the encodings offered
     * by the client via the client capability `general.positionEncodings`.
     *
     * If the client didn't provide any position encodings the only valid
     * value that a server can return is [PositionEncodingKind.UTF16].
     *
     * If omitted it defaults to [PositionEncodingKind.UTF16].
     *
     * @since 3.17.0
     */
    val positionEncoding: PositionEncodingKind?,

    /**
     * Defines how text documents are synced. Is either a detailed structure
     * defining each notification or for backwards compatibility the
     * TextDocumentSyncKind number. If omitted it defaults to
     * [TextDocumentSyncKind.None].
     */
    val textDocumentSync: OneOf<TextDocumentSyncOptions, TextDocumentSyncKind>?,

    /**
     * Defines how notebook documents are synced.
     *
     * @since 3.17.0
     */
    val notebookDocumentSync: OneOf<NotebookDocumentSyncOptions, NotebookDocumentSyncRegistrationOptions>?,

    /**
     * The server provides completion support.
     */
    val completionProvider: CompletionOptions?,

    /**
     * The server provides hover support.
     */
    val hoverProvider: OneOf<Boolean, HoverOptions>?,

    /**
     * The server provides signature help support.
     */
    val signatureHelpProvider: SignatureHelpOptions?,

    /**
     * The server provides go to declaration support.
     *
     * @since 3.14.0
     */
    val declarationProvider: OneOfThree<Boolean, DeclarationOptions, DeclarationRegistrationOptions>?,

    /**
     * The server provides goto definition support.
     */
    val definitionProvider: OneOf<Boolean, DefinitionOptions>?,

    /**
     * The server provides goto type definition support.
     *
     * @since 3.6.0
     */
    val typeDefinitionProvider: OneOfThree<Boolean, TypeDefinitionOptions, TypeDefinitionRegistrationOptions>?,

    /**
     * The server provides goto implementation support.
     *
     * @since 3.6.0
     */
    val implementationProvider: OneOfThree<Boolean, ImplementationOptions, ImplementationRegistrationOptions>?,

    /**
     * The server provides find references support.
     */
    val referencesProvider: OneOf<Boolean, ReferenceOptions>?,

    /**
     * The server provides document highlight support.
     */
    val documentHighlightProvider: OneOf<Boolean, DocumentHighlightOptions>?,

    /**
     * The server provides document symbol support.
     */
    val documentSymbolProvider: OneOf<Boolean, DocumentSymbolOptions>?,

    /**
     * The server provides code actions. The `CodeActionOptions` return type is
     * only valid if the client signals code action literal support via the
     * property `textDocument.codeAction.codeActionLiteralSupport`.
     */
    val codeActionProvider: OneOf<Boolean, CodeActionOptions>?,

    /**
     * The server provides code lens.
     */
    val codeLensProvider: CodeLensOptions?,

    /**
     * The server provides document link support.
     */
    val documentLinkProvider: DocumentLinkOptions?,

    /**
     * The server provides color provider support.
     *
     * @since 3.6.0
     */
    val colorProvider: OneOfThree<Boolean, DocumentColorOptions, DocumentColorRegistrationOptions>?,

    /**
     * The server provides document formatting.
     */
    val documentFormattingProvider: OneOf<Boolean, DocumentFormattingOptions>?,

    /**
     * The server provides document range formatting.
     */
    val documentRangeFormattingProvider: OneOf<Boolean, DocumentRangeFormattingOptions>?,

    /**
     * The server provides document formatting on typing.
     */
    val documentOnTypeFormattingProvider: DocumentOnTypeFormattingOptions?,

    /**
     * The server provides rename support. RenameOptions may only be
     * specified if the client states that it supports
     * `prepareSupport` in its initial `initialize` request.
     */
    val renameProvider: OneOf<Boolean, RenameOptions>?,

    /**
     * The server provides folding provider support.
     *
     * @since 3.10.0
     */
    val foldingRangeProvider: OneOfThree<Boolean, FoldingRangeOptions, FoldingRangeRegistrationOptions>?,

    /**
     * The server provides execute command support.
     */
    val executeCommandProvider: ExecuteCommandOptions?,

    /**
     * The server provides selection range support.
     *
     * @since 3.15.0
     */
    val selectionRangeProvider: OneOfThree<Boolean, SelectionRangeOptions, SelectionRangeRegistrationOptions>?,

    /**
     * The server provides linked editing range support.
     *
     * @since 3.16.0
     */
    val linkedEditingRangeProvider: OneOfThree<Boolean, LinkedEditingRangeOptions, LinkedEditingRangeRegistrationOptions>?,

    /**
     * The server provides call hierarchy support.
     *
     * @since 3.16.0
     */
    val callHierarchyProvider: OneOfThree<Boolean, CallHierarchyOptions, CallHierarchyRegistrationOptions>?,

    /**
     * The server provides semantic tokens support.
     *
     * @since 3.16.0
     */
    val semanticTokensProvider: OneOf<SemanticTokensOptions, SemanticTokensRegistrationOptions>?,

    /**
     * Whether server provides moniker support.
     *
     * @since 3.16.0
     */
    val monikerProvider: OneOfThree<Boolean, MonikerOptions, MonikerRegistrationOptions>?,

    /**
     * The server provides type hierarchy support.
     *
     * @since 3.17.0
     */
    val typeHierarchyProvider: OneOfThree<Boolean, TypeHierarchyOptions, TypeHierarchyRegistrationOptions>?,

    /**
     * The server provides inline values.
     *
     * @since 3.17.0
     */
    val inlineValueProvider: OneOfThree<Boolean, InlineValueOptions, InlineValueRegistrationOptions>?,

    /**
     * The server provides inlay hints.
     *
     * @since 3.17.0
     */
    val inlayHintProvider: OneOfThree<Boolean, InlayHintOptions, InlayHintRegistrationOptions>?,

    /**
     * The server has support for pull model diagnostics.
     *
     * @since 3.17.0
     */
    val diagnosticProvider: OneOf<DiagnosticOptions, DiagnosticRegistrationOptions>?,

    /**
     * The server provides workspace symbol support.
     */
    val workspaceSymbolProvider: OneOf<Boolean, WorkspaceSymbolOptions>?,

    /**
     * The server provides inline completions.
     *
     * @since 3.18.0
     */
    val inlineCompletionProvider: OneOf<Boolean, InlineCompletionOptions>?,

    /**
     * Text document specific server capabilities.
     *
     * @since 3.18.0
     */
    val textDocument: TextDocumentServerCapabilities?,

    /**
     * Workspace specific server capabilities
     */
    val workspace: WorkspaceServerCapabilities?,

    /**
     * Experimental server capabilities.
     */
    val experimental: LSPAny?
)
