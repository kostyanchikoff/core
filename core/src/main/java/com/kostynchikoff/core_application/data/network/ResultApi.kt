package com.kostynchikoff.core_application.data.network

sealed class ResultApi<out T : Any> {

    data class Success<out T : Any>(val data: T?) : ResultApi<T>()
    data class HttpError(val error: String, val code: Int = 0) : ResultApi<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is HttpError -> "Error[exception=${error}]"
        }
    }
}
