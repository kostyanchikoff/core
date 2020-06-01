package com.kostynchikoff.core_application

import android.app.Activity
import android.app.Application
import com.kostynchikoff.core_application.data.constants.CoreVariables.BASE_APOLLO_URL
import com.kostynchikoff.core_application.data.constants.CoreVariables.BASE_URL
import com.kostynchikoff.core_application.data.constants.CoreVariables.BASIC_REFRESH_AUTH_HEADER
import com.kostynchikoff.core_application.data.constants.CoreVariables.LOGIN_ACTIVITY
import com.kostynchikoff.core_application.data.constants.CoreVariables.REFRESH_TOKEN_END_POINT
import com.kostynchikoff.core_application.data.constants.CoreVariables.URLS_OF_UNNECESSARY_BEARER_TOKEN_ENDPOINTS
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 *
 */
fun Application.coreBuilder(block: CoreBuilder.() -> Unit) = CoreBuilder(this).apply(block).build()

class CoreBuilder(private val application: Application) {
    private var baseUrl: String? = null
    private var baseApolloUrl: String? = null
    private var koinModule = arrayListOf<Module>()
    private var loginActivity: Activity? = null
    private var urlsForAuthBearerTokenInterceptor = listOf<String>()
    private var baseAuthHeader: String? = null
    private var refreshTokenEndPoint: String? = null

    /**
     * Базовый url для retrofit
     * @param {String.()} строка с url для apollo
     */
    fun baseRetrofitUrl(block: () -> String) {
        baseUrl = block()
    }

    /**
     * Базовые url для клиента Apollo
     * @param {String.()} строка с url для apollo
     */
    fun baseApolloUrl(block: () -> String) {
        baseApolloUrl = block()
    }

    /**
     * Модуля для koin
     * @param {List.()} список с модулями
     */
    fun koinModule(block: () -> ArrayList<Module>) {
        koinModule = block()
    }

    /**
     * Список url которым не требуються header-ы
     * @param {List.()} список с url
     */
    fun endpointUrlsNecessaryForAuthBearer(block: () -> List<String>) {
        urlsForAuthBearerTokenInterceptor = block()
    }

    /**
     * Базовый header для refresh token
     * @param {String.()} строка с базовым header-ом
     */
    fun baseHeaderRefreshToken(block: () -> String) {
        baseAuthHeader = block()
    }

    /**
     * передаем запрос для refresh token например "api/refresh/token"
     * @param {String.()} строка с end point
     */
    fun refreshTokenEndPoint(block: () -> String) {
        refreshTokenEndPoint = block()
    }


    /**
     * При испечении токена перенаправляем на данное activity
     * @param {Activity.()} объект activity
     */
    fun loginActivity(block: () -> Activity) {
        loginActivity = block()
    }

    fun build() {
        BASE_URL = baseUrl.orEmpty()
        LOGIN_ACTIVITY = loginActivity
        BASE_APOLLO_URL = baseApolloUrl.orEmpty()
        URLS_OF_UNNECESSARY_BEARER_TOKEN_ENDPOINTS = urlsForAuthBearerTokenInterceptor
        BASIC_REFRESH_AUTH_HEADER = baseAuthHeader.orEmpty()
        REFRESH_TOKEN_END_POINT = refreshTokenEndPoint.orEmpty()


        startKoin {
            androidLogger()
            androidContext(this@CoreBuilder.application.applicationContext)
            modules(koinModule)
        }
    }
}