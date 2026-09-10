@file:Suppress("DEPRECATION")

package com.axiom.lsp.server.internal

import com.axiom.lsp.CallHierarchyIncomingCall
import com.axiom.lsp.CallHierarchyIncomingCallsParams
import com.axiom.lsp.CallHierarchyItem
import com.axiom.lsp.CallHierarchyOutgoingCall
import com.axiom.lsp.CallHierarchyOutgoingCallsParams
import com.axiom.lsp.CallHierarchyPrepareParams
import com.axiom.lsp.CodeAction
import com.axiom.lsp.CodeActionParams
import com.axiom.lsp.CodeLens
import com.axiom.lsp.CodeLensParams
import com.axiom.lsp.ColorInformation
import com.axiom.lsp.ColorPresentation
import com.axiom.lsp.ColorPresentationParams
import com.axiom.lsp.Command
import com.axiom.lsp.CompletionItem
import com.axiom.lsp.CompletionList
import com.axiom.lsp.CompletionParams
import com.axiom.lsp.DeclarationParams
import com.axiom.lsp.DefinitionParams
import com.axiom.lsp.DidChangeTextDocumentParams
import com.axiom.lsp.DidCloseTextDocumentParams
import com.axiom.lsp.DidOpenTextDocumentParams
import com.axiom.lsp.DidSaveTextDocumentParams
import com.axiom.lsp.DocumentColorParams
import com.axiom.lsp.DocumentDiagnosticParams
import com.axiom.lsp.DocumentDiagnosticReport
import com.axiom.lsp.DocumentFormattingParams
import com.axiom.lsp.DocumentHighlight
import com.axiom.lsp.DocumentHighlightParams
import com.axiom.lsp.DocumentLink
import com.axiom.lsp.DocumentLinkParams
import com.axiom.lsp.DocumentOnTypeFormattingParams
import com.axiom.lsp.DocumentRangeFormattingParams
import com.axiom.lsp.DocumentRangesFormattingParams
import com.axiom.lsp.DocumentSymbol
import com.axiom.lsp.DocumentSymbolParams
import com.axiom.lsp.FoldingRange
import com.axiom.lsp.FoldingRangeParams
import com.axiom.lsp.Hover
import com.axiom.lsp.HoverParams
import com.axiom.lsp.ImplementationParams
import com.axiom.lsp.InlayHint
import com.axiom.lsp.InlayHintParams
import com.axiom.lsp.InlineCompletionItem
import com.axiom.lsp.InlineCompletionList
import com.axiom.lsp.InlineCompletionParams
import com.axiom.lsp.InlineValue
import com.axiom.lsp.InlineValueParams
import com.axiom.lsp.LinkedEditingRangeParams
import com.axiom.lsp.LinkedEditingRanges
import com.axiom.lsp.Location
import com.axiom.lsp.LocationLink
import com.axiom.lsp.Moniker
import com.axiom.lsp.MonikerParams
import com.axiom.lsp.PrepareRenameDefaultBehavior
import com.axiom.lsp.PrepareRenameParams
import com.axiom.lsp.PrepareRenameResult
import com.axiom.lsp.Range
import com.axiom.lsp.ReferenceParams
import com.axiom.lsp.RenameParams
import com.axiom.lsp.SelectionRange
import com.axiom.lsp.SelectionRangeParams
import com.axiom.lsp.SemanticTokens
import com.axiom.lsp.SemanticTokensDelta
import com.axiom.lsp.SemanticTokensDeltaParams
import com.axiom.lsp.SemanticTokensParams
import com.axiom.lsp.SemanticTokensRangeParams
import com.axiom.lsp.SignatureHelp
import com.axiom.lsp.SignatureHelpParams
import com.axiom.lsp.SymbolInformation
import com.axiom.lsp.TextEdit
import com.axiom.lsp.TypeDefinitionParams
import com.axiom.lsp.TypeHierarchyItem
import com.axiom.lsp.TypeHierarchyPrepareParams
import com.axiom.lsp.TypeHierarchySupertypesParams
import com.axiom.lsp.WillSaveTextDocumentParams
import com.axiom.lsp.WorkspaceEdit
import com.axiom.lsp.server.TextDocumentService
import com.axiom.lsp.types.OneOf
import com.axiom.lsp.types.OneOfThree
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement

internal class TextDocumentServiceImpl(val connection: JsonRpcConnection, val json: Json) : TextDocumentService {
    private suspend inline fun <reified T, reified Params> sendRequest(method: String, params: Params? = null): T {
        return connection.sendRequest("textDocument/$method", params)
    }

    private suspend inline fun <reified Params> sendNotification(method: String, params: Params? = null) {
        connection.sendNotification("textDocument/$method", json.encodeToJsonElement(params))
    }

    override suspend fun didOpen(params: DidOpenTextDocumentParams) {
        sendNotification("didOpen", params)
    }

    override suspend fun didChange(params: DidChangeTextDocumentParams) {
        sendNotification("didChange", params)
    }

    override suspend fun willSave(params: WillSaveTextDocumentParams) {
        sendNotification("willSave", params)
    }

    override suspend fun willSaveWaitUntil(params: WillSaveTextDocumentParams): List<TextEdit>? {
        return sendRequest("willSaveWaitUntil", params)
    }

    override suspend fun didSave(params: DidSaveTextDocumentParams) {
        sendNotification("didSave", params)
    }

    override suspend fun didClose(params: DidCloseTextDocumentParams) {
        sendNotification("didClose", params)
    }

    override suspend fun declaration(params: DeclarationParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("declaration", params)
    }

    override suspend fun definition(params: DefinitionParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("definition", params)
    }

    override suspend fun typeDefinition(params: TypeDefinitionParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("typeDefinition", params)
    }

    override suspend fun implementation(params: ImplementationParams): OneOf<List<Location>, List<LocationLink>>? {
        return sendRequest("implementation", params)
    }

    override suspend fun references(params: ReferenceParams): List<LocationLink>? {
        return sendRequest("references", params)
    }

    override suspend fun prepareCallHierarchy(params: CallHierarchyPrepareParams): List<CallHierarchyItem>? {
        return sendRequest("prepareCallHierarchy", params)
    }

    override suspend fun callHierarchyIncomingCalls(params: CallHierarchyIncomingCallsParams): List<CallHierarchyIncomingCall>? {
        return connection.sendRequest("callHierarchy/incomingCalls", params)
    }

    override suspend fun callHierarchyOutgoingCalls(params: CallHierarchyOutgoingCallsParams): List<CallHierarchyOutgoingCall>? {
        return connection.sendRequest("callHierarchy/outgoingCalls", params)
    }

    override suspend fun prepareTypeHierarchy(params: TypeHierarchyPrepareParams): List<TypeHierarchyItem>? {
        return sendRequest("prepareTypeHierarchy", params)
    }

    override suspend fun typeHierarchySupertypes(params: TypeHierarchySupertypesParams): List<TypeHierarchyItem>? {
        return connection.sendRequest("typeHierarchy/supertypes", params)
    }

    override suspend fun documentHighlight(params: DocumentHighlightParams): List<DocumentHighlight>? {
        return sendRequest("documentHighlight", params)
    }

    override suspend fun documentLink(params: DocumentLinkParams): List<DocumentLink>? {
        return sendRequest("documentLink", params)
    }

    override suspend fun resolveDocumentLink(unresolved: DocumentLink): DocumentLink {
        return connection.sendRequest("documentLink/resolve", unresolved)
    }

    override suspend fun hover(params: HoverParams): Hover? {
        return sendRequest("hover", params)
    }

    override suspend fun codeLens(params: CodeLensParams): List<CodeLens>? {
        return sendRequest("codeLens", params)
    }

    override suspend fun resolveCodeLens(unresolved: CodeLens): CodeLens {
        return connection.sendRequest("codeLens/resolve", unresolved)
    }

    override suspend fun foldingRange(params: FoldingRangeParams): List<FoldingRange>? {
        return sendRequest("foldingRange", params)
    }

    override suspend fun selectionRange(params: SelectionRangeParams): List<SelectionRange>? {
        return sendRequest("selectionRange", params)
    }

    override suspend fun documentSymbol(params: DocumentSymbolParams): OneOf<List<DocumentSymbol>, List<SymbolInformation>>? {
        return sendRequest("documentSymbol", params)
    }

    override suspend fun semanticTokensFull(params: SemanticTokensParams): SemanticTokens? {
        return sendRequest("semanticTokens/full", params)
    }

    override suspend fun semanticTokensFullDelta(params: SemanticTokensDeltaParams): OneOf<SemanticTokens, SemanticTokensDelta>? {
        return sendRequest("semanticTokens/full/delta", params)
    }

    override suspend fun semanticTokensRange(params: SemanticTokensRangeParams): SemanticTokens? {
        return sendRequest("semanticTokens/range", params)
    }

    override suspend fun inlayHint(params: InlayHintParams): List<InlayHint>? {
        return sendRequest("inlayHint", params)
    }

    override suspend fun resolveInlayHint(unresolved: InlayHint): InlayHint {
        return connection.sendRequest("inlayHint/resolve", unresolved)
    }

    override suspend fun inlineValue(params: InlineValueParams): List<InlineValue>? {
        return sendRequest("inlineValue", params)
    }

    override suspend fun moniker(params: MonikerParams): List<Moniker>? {
        return sendRequest("moniker", params)
    }

    override suspend fun completion(params: CompletionParams): OneOf<List<CompletionItem>, CompletionList>? {
        return sendRequest("completion", params)
    }

    override suspend fun resolveCompletionItem(unresolved: CompletionItem): CompletionItem {
        return connection.sendRequest("completionItem/resolve", unresolved)
    }

    override suspend fun diagnostic(params: DocumentDiagnosticParams): DocumentDiagnosticReport {
        return sendRequest("diagnostic", params)
    }

    override suspend fun signatureHelp(params: SignatureHelpParams): SignatureHelp? {
        return sendRequest("signatureHelp", params)
    }

    override suspend fun codeAction(params: CodeActionParams): List<OneOf<Command, CodeAction>>? {
        return sendRequest("codeAction", params)
    }

    override suspend fun resolveCodeAction(unresolved: CodeAction): CodeAction {
        return connection.sendRequest("codeAction/resolve", unresolved)
    }

    override suspend fun documentColor(params: DocumentColorParams): List<ColorInformation> {
        return sendRequest("documentColor", params)
    }

    override suspend fun colorPresentation(params: ColorPresentationParams): List<ColorPresentation> {
        return sendRequest("colorPresentation", params)
    }

    override suspend fun formatting(params: DocumentFormattingParams): List<TextEdit>? {
        return sendRequest("formatting", params)
    }

    override suspend fun rangeFormatting(params: DocumentRangeFormattingParams): List<TextEdit>? {
        return sendRequest("rangeFormatting", params)
    }

    override suspend fun rangesFormatting(params: DocumentRangesFormattingParams): List<TextEdit>? {
        return sendRequest("rangesFormatting", params)
    }

    override suspend fun onTypeFormatting(params: DocumentOnTypeFormattingParams): List<TextEdit>? {
        return sendRequest("onTypeFormatting", params)
    }

    override suspend fun rename(params: RenameParams): WorkspaceEdit? {
        return sendRequest("rename", params)
    }

    override suspend fun prepareRename(params: PrepareRenameParams): OneOfThree<Range, PrepareRenameResult, PrepareRenameDefaultBehavior>? {
        return sendRequest("prepareRename", params)
    }

    override suspend fun linkedEditingRange(params: LinkedEditingRangeParams): LinkedEditingRanges? {
        return sendRequest("linkedEditingRange", params)
    }

    override suspend fun inlineCompletion(params: InlineCompletionParams): OneOf<List<InlineCompletionItem>, InlineCompletionList>? {
        return sendRequest("inlineCompletion", params)
    }
}
