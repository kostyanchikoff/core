package com.kostynchikoff.core_application.utils.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.exception.ApolloHttpException
import com.apollographql.apollo.exception.ApolloNetworkException
import com.apollographql.apollo.exception.ApolloParseException
import com.kostynchikoff.core_application.data.network.ResultApi
import com.kostynchikoff.core_application.utils.exeption.ApolloSuccessException
import java.net.HttpURLConnection
import javax.net.ssl.SSLHandshakeException


suspend fun <A : Operation.Data, B : Any, C : Operation.Variables> ApolloClient.apolloSafeApiCall(
    call: suspend () -> Query<A, B, C>
): ResultApi<B> {
    return try {
        val result = query(call.invoke()).toDeferred().await()
        val isNullError = result.errors?.isNullOrEmpty() ?: true
        if (!isNullError) {
            throw ApolloSuccessException(result.errors?.get(0)?.message.orEmpty())
        }
        ResultApi.Success(result.data)
    } catch (e: ApolloException) {
        handleApolloException(e)
    }
}


fun <T : Any> handleApolloException(e: ApolloException): ResultApi<T> {
    return when (e) {
        is ApolloHttpException -> when (e.code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> ResultApi.HttpError(
                "Срок действия сессии истек",
                e.code()
            )
            HttpURLConnection.HTTP_NOT_FOUND -> ResultApi.HttpError("Данного запроса не существует", e.code())
            HttpURLConnection.HTTP_SERVER_ERROR -> ResultApi.HttpError("Внутренняя ошибка сервера", e.code())
            else -> ResultApi.HttpError("Ошибка сервера", e.code())
        }

        is ApolloNetworkException -> ResultApi.HttpError("Проверте подключение к интернету")
        is ApolloSuccessException -> ResultApi.HttpError(e.message.orEmpty())
        is SSLHandshakeException -> ResultApi.HttpError("Ошибка сертификата")
        is ApolloParseException -> ResultApi.HttpError("Невозможно распарсить данные")
        else -> ResultApi.HttpError("Неизветная ошибка")
    }
}


