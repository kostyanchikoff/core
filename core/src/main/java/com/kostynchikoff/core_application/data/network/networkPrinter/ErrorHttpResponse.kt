package com.kostynchikoff.core_application.data.network.networkPrinter

import com.kostynchikoff.core_application.utils.parseJson

class ErrorHttpResponse : NetworkErrorHttpPrinter<DefaultError> {
    override fun print(response: String?, default: String?): DefaultError {
        return parseJson<DefaultError>(response)  ?: DefaultError(message = default)
    }
}

class DefaultError(val error: String? = null, val message: String? = null)


