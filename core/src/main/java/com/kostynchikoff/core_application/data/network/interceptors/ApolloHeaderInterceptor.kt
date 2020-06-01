package com.kostynchikoff.core_application.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor использовать только для клиента Apollo
 * Возможно потребуеться токен
 */
class ApolloHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}