package com.kostynchikoff.core_application.utils.network

import com.google.gson.JsonParseException
import com.kostynchikoff.core_application.data.network.networkPrinter.ErrorHttpResponse
import com.kostynchikoff.core_application.data.network.ResultApi
import com.kostynchikoff.core_application.data.network.networkPrinter.NetworkErrorHttpPrinter
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * Покрыт тестами на 100%
 */


/**
 * Функция обработки http и прочих ошибок
 * @param call suspend функция
 */
suspend fun <T : Any> safeApiCall(
    call: suspend () -> T
): ResultApi<T> {
    return try {
        ResultApi.Success(call.invoke())
    } catch (e: Exception) {
        handleException(e, ErrorHttpResponse())
    }
}

/**
 * Функция обработки http и прочих ошибок
 * @param call suspend функция
 * @param errorPrinter (необязательный) используеться для обработки ошибок с применением полей в теле ошибки
 */
suspend fun <T : Any, V> safeApiCall(
    call: suspend () -> T,
    errorPrinter: NetworkErrorHttpPrinter<V>
): ResultApi<T> {
    return try {
        ResultApi.Success(call.invoke())
    } catch (e: Exception) {
        handleException(e, errorPrinter)
    }
}


fun <T : Any, V> handleException(
    e: Exception,
    customResponseError: NetworkErrorHttpPrinter<V>?
): ResultApi<T> =
    when (e) {
        is HttpException -> handleHttpException(e, customResponseError)
        is SocketTimeoutException -> ResultApi.HttpError("Сервер не отвечает")
        is SSLHandshakeException -> ResultApi.HttpError("Возникли проблемы с сертификатом")
        is JsonParseException -> ResultApi.HttpError("Ошибка обработки запроса")
        is EOFException -> ResultApi.HttpError("Ошибка загрузки, попробуйте еще раз")
        is ConnectException,
        is UnknownHostException -> ResultApi.HttpError("Возникли проблемы с интернетом")
        else -> ResultApi.HttpError("Ошибка : ${e.javaClass.simpleName} ${e.localizedMessage}")
    }

private fun <T : Any, V> handleHttpException(
    e: HttpException,
    customResponseError: NetworkErrorHttpPrinter<V>?
): ResultApi<T> {
    val errorBody = e.response()?.errorBody()?.string().orEmpty()
    return when (e.code()) {
        HttpURLConnection.HTTP_UNAUTHORIZED -> {
            val reason = "Срок действия сессии истек"
            ResultApi.HttpError(customResponseError?.print(errorBody, reason), e.code())
        }
        else -> {
            ResultApi.HttpError(customResponseError?.print(errorBody, "Ошибка"), e.code())
        }
    }
}