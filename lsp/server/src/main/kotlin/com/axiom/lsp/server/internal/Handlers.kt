package com.axiom.lsp.server.internal

import com.axiom.lsp.NotificationMessage
import com.axiom.lsp.RequestId
import com.axiom.lsp.RequestMessage
import com.axiom.lsp.ResponseMessage

internal typealias ResponseHandler = suspend (ResponseMessage) -> Unit
internal typealias RequestHandler = suspend (RequestMessage) -> ResponseMessage
internal typealias NotificationHandler = suspend (RequestId?, NotificationMessage) -> Unit
