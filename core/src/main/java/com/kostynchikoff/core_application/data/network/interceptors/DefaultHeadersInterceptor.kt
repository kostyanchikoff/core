package com.kostynchikoff.core_application.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class DefaultHeadersInterceptor(
    private val locale: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept-Language", locale)
            .build()

        return chain.proceed(request)
    }

}