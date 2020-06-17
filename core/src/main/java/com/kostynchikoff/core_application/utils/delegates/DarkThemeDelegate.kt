package com.kostynchikoff.core_application.utils.delegates

import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import com.kostynchikoff.core_application.data.constants.CoreConstant.MIN_VERSION_SUPPORT_DARK_MODE
import com.kostynchikoff.core_application.data.prefs.SourcesLocalDataSource
import com.kostynchikoff.core_application.utils.os.getOSVersion
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.RuntimeException


/**
 * Делегат служит для определения темы и смены темы приложения
 */
interface DarkTheme {

    /**
     * Получить тему приложения
     */
    fun initTheme(window: Window, defaultTheme: Theme)

    /**
     * Задать тему приложения для того чтобы задать тему используте Activity.setTheme
     */
    fun setTheme(theme: Theme, window: Window)

}


class DarkThemeDelegate : DarkTheme, KoinComponent {


    private val sourcesLocalStorage by inject<SourcesLocalDataSource>()

    override fun initTheme(window: Window, defaultTheme: Theme) {
        val theme = sourcesLocalStorage.getTheme() ?: defaultTheme.name
        makeTheme(theme, window)
    }

    override fun setTheme(theme: Theme, window: Window) {
        if (getOSVersion() < MIN_VERSION_SUPPORT_DARK_MODE) {
            throw RuntimeException("Устройство не поддерживает dart mode")
        }

        sourcesLocalStorage.setTheme(theme)
        makeTheme(sourcesLocalStorage.getTheme(), window)
    }


    private fun makeTheme(theme: String?, window: Window) {
        if (getOSVersion() < MIN_VERSION_SUPPORT_DARK_MODE) {
            return
        }
        when (theme) {
            Theme.DARK.name -> makeDarkTheme(window)
            Theme.APP.name -> makeLightTheme(window)
            else -> makeDarkTheme(window)
        }
    }

    private fun makeDarkTheme(window: Window) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        window.decorView.systemUiVisibility = 0
    }

    private fun makeLightTheme(window: Window) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

}

enum class Theme {
    DARK, APP
}


