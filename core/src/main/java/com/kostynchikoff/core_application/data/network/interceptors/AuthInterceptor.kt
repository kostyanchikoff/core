package com.kostynchikoff.core_application.data.network.interceptors

import com.kostynchikoff.core_application.data.prefs.SecurityDataSource
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.net.HttpURLConnection

class AuthInterceptor() : Interceptor, KoinComponent {

    private val pref by inject<SecurityDataSource>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request().newBuilder().addHeader("Authorization", "Bearer ${pref.getAccessToken()}")
                .build()
        val response = chain.proceed(request)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            pref.clearAuthorizedUserData()
        }

        return response
    }
}