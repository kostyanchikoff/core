package com.kostynchikoff.core_application.data.network.networkPrinter

import com.kostynchikoff.core_application.data.constants.CoreConstant
import com.kostynchikoff.core_application.utils.parseJson

class ErrorHttpResponse : NetworkErrorHttpPrinter<String> {
    override fun print(response: String?, default: String?): String {
        return parseJson<DefaultError>(response)?.getErrorMessage()  ?: default ?: CoreConstant.EMPTY
    }
}

class DefaultError(val error: String? = null, private val message: String? = null){
    fun getErrorMessage() = error ?: message
}


