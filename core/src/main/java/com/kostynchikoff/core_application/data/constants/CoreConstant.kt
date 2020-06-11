package com.kostynchikoff.core_application.data.constants

import com.kostynchikoff.core_application.data.constants.CoreVariables.OPERATOR

object CoreConstant{
    const val EMPTY = ""

    const val CONNECTION_TIMEOUT = 60000L
    const val READ_TIMEOUT = 60000L
    const val PERMISSION_DENIED = -1

    const val PREF_ACCESS_TEMP_PIN_CODE = "PREF_ACCESS_TEMP_PIN_CODE"
    const val PREF_ACCESS_PIN_CODE = "PREF_ACCESS_PIN_CODE"
    const val PREF_AUTH_ACCESS_TOKEN = "PREF_AUTH_ACCESS_TOKEN"
    const val PREF_AUTH_REFRESH_TOKEN = "PREF_AUTH_REFRESH_TOKEN"
    const val PREF_AUTH_SESSION = "PREF_AUTH_SESSION"
    const val PREF_THEME = "PREF_THEME"
    const val PREF_SESSION_TIME = "PREF_SESSION_TIME"
    const val PREF_SECRET_SHARED = "SECRET_SHARED_PREF"
    const val PREF_IS_USE_FINGER_PRINT = "PREF_IS_USE_FINGER_PRINT"
    const val PREF_IS_USE_FACE_ID = "PREF_IS_USE_FACE_ID"

    const val PREF_PHONE_NUMBER = "PREF_PHONE_NUMBER"

    const val ARG_REDIRECT = "ARG_REDIRECT"
    const val ARG_ANIM = "ARG_ANIM"

    const val AUTHORIZATION  = "Authorization"
    const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
}