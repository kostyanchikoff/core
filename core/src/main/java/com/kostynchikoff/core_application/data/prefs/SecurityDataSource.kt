package com.kostynchikoff.core_application.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.kostynchikoff.core_application.data.constants.CoreConstant.EMPTY
import com.kostynchikoff.core_application.data.constants.CoreConstant.PREF_ACCESS_PIN_CODE
import com.kostynchikoff.core_application.data.constants.CoreConstant.PREF_ACCESS_TEMP_PIN_CODE
import com.kostynchikoff.core_application.data.constants.CoreConstant.PREF_AUTH_ACCESS_TOKEN
import com.kostynchikoff.core_application.data.constants.CoreConstant.PREF_AUTH_REFRESH_TOKEN
import com.kostynchikoff.core_application.data.constants.CoreConstant.PREF_AUTH_SESSION

/**
 * Возможность хранить защишенные данные (токен, пароль, логин)
 */
class SecurityDataSource(private val pref: SharedPreferences) {

    /**
     * Делаем logout пользователя
     */
    fun clearAuthorizedUserData() {
        pref.edit { remove(PREF_ACCESS_PIN_CODE) }
        pref.edit { remove(PREF_AUTH_SESSION) }
        pref.edit { remove(PREF_AUTH_ACCESS_TOKEN) }
        pref.edit { remove(PREF_AUTH_REFRESH_TOKEN) }
    }

    /**
     * Сохранить пин код
     */
    fun setAccessPinCode(pinCode: String) = pref.edit { putString(PREF_ACCESS_PIN_CODE, pinCode) }

    /**
     * Получить пин код
     */
    fun getAccessPinCode() = pref.getString(PREF_ACCESS_PIN_CODE, EMPTY)

    /**
     * Удалить пин код
     */
    fun clearAccessPinCode() = pref.edit { remove(PREF_ACCESS_PIN_CODE) }

    /**
     * Сохранить временный пин код
     */
    fun setAccessTempPinCode(pinCode: String) =
        pref.edit { putString(PREF_ACCESS_TEMP_PIN_CODE, pinCode) }

    /**
     * Получить временный пин код
     */
    fun getAccessTempPinCode() = pref.getString(PREF_ACCESS_TEMP_PIN_CODE, EMPTY)

    /**
     * Удалить временный пин код
     */
    fun cleanTempCode() = pref.edit { remove(PREF_ACCESS_TEMP_PIN_CODE) }

    /**
     * Сохранить auth access token
     */
    fun setAccessToken(token: String) = pref.edit { putString(PREF_AUTH_ACCESS_TOKEN, token) }

    /**
     * Получить auth access token
     */
    fun getAccessToken() = pref.getString(PREF_AUTH_ACCESS_TOKEN, EMPTY)

    /**
     * Сохранить auth refresh token
     */
    fun setRefreshToken(token: String) = pref.edit { putString(PREF_AUTH_REFRESH_TOKEN, token) }

    /**
     * Получить auth refresh token
     */
    fun getRefreshToken() = pref.getString(PREF_AUTH_REFRESH_TOKEN, EMPTY)

    /**
     * Сохранить auth session
     */
    fun setSession(session: String) = pref.edit { putString(PREF_AUTH_SESSION, session) }

    /**
     * Получить auth session
     */
    fun getSession() = pref.getString(PREF_AUTH_SESSION, EMPTY)

}