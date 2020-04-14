package com.kostynchikoff.core_application.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import com.kostynchikoff.core_application.data.constants.CoreConstant.EMPTY
import com.kostynchikoff.core_application.data.constants.CoreConstant.TOKEN_PREF

/**
 * Возможность хранить защишенные данные (токен, пароль, логин)
 */
class SecurityDataSource(private val pref: SharedPreferences) {

    fun addToken(token: String) = pref.edit { putString(TOKEN_PREF, token) }

    fun removeToken() = pref.edit { remove(TOKEN_PREF) }

    fun getToken() = pref.getString(TOKEN_PREF, EMPTY)
}