package com.kostynchikoff.core

import android.app.Application
import com.kostynchikoff.core_application.coreBuilder
import com.kostynchikoff.core_application.di.coreModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        coreBuilder {

            baseUrl { "http://192.168.0.101:3000" }

            koinModule {
                arrayListOf(
                    coreModule,
                    httpModule,
                    dataSourceModule,
                    useCaseModule,
                    repositoryModule,
                    viewModelModule
                )
            }

            loginActivity{ LoginActivity() }

        }
    }
}