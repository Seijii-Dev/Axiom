package com.axiom.lsp.server

import com.axiom.lsp.ResponseError

class ResponseErrorException(error: ResponseError) : Exception() {
    val code = error.code
    val data = error.data

    override val message: String = "Request failed: ${error.message}. Data: ${error.data}"
}

internal fun ResponseError.wrap() = ResponseErrorException(this)
