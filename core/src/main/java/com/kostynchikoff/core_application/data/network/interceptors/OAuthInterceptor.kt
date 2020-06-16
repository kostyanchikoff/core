package com.kostynchikoff.core_application.data.network.interceptors

import com.google.gson.Gson
import com.kostynchikoff.core_application.data.constants.CoreConstant.AUTHORIZATION
import com.kostynchikoff.core_application.data.constants.CoreConstant.GRANT_TYPE_REFRESH_TOKEN
import com.kostynchikoff.core_application.data.constants.CoreVariables.BASE_URL
import com.kostynchikoff.core_application.data.constants.CoreVariables.BASIC_REFRESH_AUTH_HEADER
import com.kostynchikoff.core_application.data.constants.CoreVariables.OPERATOR
import com.kostynchikoff.core_application.data.constants.CoreVariables.REFRESH_TOKEN_END_POINT
import com.kostynchikoff.core_application.data.constants.CoreVariables.URLS_OF_UNNECESSARY_BEARER_TOKEN_ENDPOINTS
import com.kostynchikoff.core_application.data.prefs.SecurityDataSource
import com.kostynchikoff.core_application.data.user.AuthRefreshTokenDTO
import com.kostynchikoff.core_application.data.user.RefreshTokenRequestDTO
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.net.HttpURLConnection

class OAuthInterceptor : Interceptor, KoinComponent {

    private val pref by inject<SecurityDataSource>()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)
        val urlOfRequest = originalRequest.url.toString().substringAfter(BASE_URL)


        if (!URLS_OF_UNNECESSARY_BEARER_TOKEN_ENDPOINTS.contains(urlOfRequest) &&
            response.code == HttpURLConnection.HTTP_UNAUTHORIZED
        ) {

            if (pref.getAccessToken() != null) {
                val newRequest = originalRequest.newBuilder()
                    .header(AUTHORIZATION, "Bearer ${pref.getAccessToken()}")
                    .build()
                val responseLocal = chain.proceed(newRequest)
                if (responseLocal.code == HttpURLConnection.HTTP_OK) {
                    return responseLocal
                }
            }

            val refreshTokenBody = RefreshTokenRequestDTO(
                pref.getRefreshToken().orEmpty(),
                GRANT_TYPE_REFRESH_TOKEN,
                OPERATOR
            )
            val body = Gson().toJson(refreshTokenBody).toString().toRequestBody()
            val refreshTokenRequest = originalRequest
                .newBuilder()
                .post(body)
                .url(BASE_URL + REFRESH_TOKEN_END_POINT)
                .addHeaders(BASIC_REFRESH_AUTH_HEADER)
                .build()

            val refreshResponse = chain.proceedDeletingTokenOnError(refreshTokenRequest)
            if (refreshResponse.isSuccessful) {
                val refreshedToken = Gson().fromJson(
                    refreshResponse.body?.string(),
                    AuthRefreshTokenDTO::class.java
                )
                pref.setAccessToken(refreshedToken.access_token)
                pref.setRefreshToken(refreshedToken.refresh_token)
                pref.setSession(refreshedToken.session)
                val token = "${refreshedToken.token_type.orEmpty()} ${refreshedToken.access_token}"
                val newCall = originalRequest.newBuilder().addHeaders(token).build()
                chain.proceedDeletingTokenOnError(newCall)
            } else {
                chain.proceedDeletingTokenOnError(chain.request())
            }
        } else if (URLS_OF_UNNECESSARY_BEARER_TOKEN_ENDPOINTS.contains(urlOfRequest) && response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val oldResponse: Response = chain.proceed(chain.request())
            return oldResponse.newBuilder().code(HttpURLConnection.HTTP_INTERNAL_ERROR).build()
        }

        return response
    }

    private fun Interceptor.Chain.proceedDeletingTokenOnError(request: Request): Response {
        val response = proceed(request)
        pref.clearAuthorizedUserData()
        return response
    }

    private fun Request.Builder.addHeaders(token: String) =
        this.apply {
            header(AUTHORIZATION, token)
        }
}