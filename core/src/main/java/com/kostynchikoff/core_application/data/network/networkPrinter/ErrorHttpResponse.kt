package com.kostynchikoff.core_application.data.network.networkPrinter

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class ErrorHttpResponse : NetworkErrorPrinter<String> {

    // TODO удалить после тестирования
//    companion object {
//        /**
//         * Парсинг ответа сервера вручную в объект [ErrorResponse]
//         *
//         * Этот метод должен оставаться приватным
//         * (ничего страшного, что каждый раз создаётся новый объект)
//         */
//        @Throws(JsonSyntaxException::class)
//        private fun from(response: String?): ErrorHttpResponse {
//            return Gson().fromJson(response, ErrorHttpResponse::class.java)
//        }
//
//        fun print(response: String?, default: String) = try {
//            from(
//                response
//            ).print(default)
//        } catch (e: Exception) {
//            default
//        }
//    }

    private val error: String? = null
    private val message: String? = null

    override fun print(default: String): String {
        return message ?: error ?: default
    }

    override fun print(response: String?, default: String) = try {
        from(
            response
        ).print(default)
    } catch (e: Exception) {
        default
    }

    override fun from(response: String?) = Gson().fromJson(response, ErrorHttpResponse::class.java)


}


