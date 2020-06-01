package com.kostynchikoff.core_application

import android.app.Activity
import android.app.Application
import com.kostynchikoff.core_application.data.constants.CoreVariables.BASE_APOLLO_URL
import com.kostynchikoff.core_application.data.constants.CoreVariables.BASE_URL
import com.kostynchikoff.core_application.data.constants.CoreVariables.LOGIN_ACTIVITY
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

    fun baseUrl(block: () -> String) {
        baseUrl = block()
    }

    fun baseApolloUrl(block: () -> String) {
        baseApolloUrl = block()
    }

    fun koinModule(block: () -> ArrayList<Module>) {
        koinModule = block()
    }

    /**
     * При испечении токена перенаправляем на данное activity
     */
    fun loginActivity(block: () -> Activity) {
        loginActivity = block()
    }

    fun build() {
        BASE_URL = baseUrl.orEmpty()
        LOGIN_ACTIVITY = loginActivity
        BASE_APOLLO_URL = baseApolloUrl.orEmpty()
        startKoin {
            androidLogger()
            androidContext(this@CoreBuilder.application.applicationContext)
            modules(koinModule)
        }
    }
}